/*
 * This file is part of Grave, licensed under the MIT License (MIT).
 *
 * Copyright (c) Azatik <http://azatik.github.io>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.azatik.grave.events;

import io.github.azatik.grave.Grave;
import io.github.azatik.grave.database.DataBase;
import static io.github.azatik.grave.database.DataBase.getLastGrave;
import io.github.azatik.grave.utils.SignManipulator;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.translator.ConfigurateTranslator;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.IndirectEntityDamageSource;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import static org.spongepowered.api.util.Direction.SOUTH;
import org.spongepowered.api.util.PositionOutOfBoundsException;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class EventDropItem {

    @Listener
    public void onDropItem(DropItemEvent.Destruct event) throws SQLException, IOException {
        Text msgDied;
        Text noGrave = Texts.of(TextColors.DARK_RED, "Могила не образовалась по какой-то причине." + "\n"
                + "Ты можешь её восстановить командой /grave repair [# могилы]");

        SignManipulator dataofsign = new SignManipulator();
        List<Entity> entities = event.getEntities();
        ArrayList<DataContainer> itemContainers = new ArrayList();
        ArrayList<String> itemSerializedContainers = new ArrayList();

        boolean causeOut = false;
        if (causeOut) {
            Grave.getInstance().getLogger().info(event.getCause().toString());
        }

        boolean playerCause = event.getCause().first(Player.class).isPresent();
        if (playerCause) {
            boolean damageCause = event.getCause().first(DamageSource.class).isPresent();
            boolean entityDamageSource = event.getCause().first(IndirectEntityDamageSource.class).isPresent();
            boolean isPlayerIndirectSource = false;
            if (event.getCause().first(IndirectEntityDamageSource.class).isPresent()) {
                isPlayerIndirectSource = "player".equals(event.getCause().first(IndirectEntityDamageSource.class).get().getIndirectSource().getType().getName());
            }
            if ((playerCause && damageCause && !entityDamageSource) || (playerCause && isPlayerIndirectSource)) {
                boolean ownerCause = event.getCause().containsNamed("Owner");
                if ((playerCause && !ownerCause)) {
                    Player player = event.getCause().first(Player.class).get();
                    World world = player.getWorld();
                    
                    //Here is the logic of setting the grave.
                    Location LocSign = player.getLocation();

                    entities.stream().forEach((Entity entity) -> {
                        itemContainers.add(entity.getValue(Keys.REPRESENTED_ITEM).get().get().toContainer());
                    });

                    itemContainers.stream().forEach((DataContainer container) -> {
                        try {
                            StringWriter writer = new StringWriter();
                            HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setSink(new Callable<BufferedWriter>() {
                                @Override
                                public BufferedWriter call() {
                                    return new BufferedWriter(writer);
                                }
                            }).build();

                            loader.save(ConfigurateTranslator.instance().translateData(container));
                            String toString = writer.toString();
                            itemSerializedContainers.add(toString);
                            loader = null;
                        } catch (IOException ex) {
                            Logger.getLogger(EventDropItem.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });

                    int playerId = DataBase.getPlayerId(player);
                    String executeInGrave = ("insert into graves(player_id, world_name, coord_x, coord_y, coord_z) values (%s, '%s', %s, %s, %s);");
                    String executeFormatInGrave = String.format(executeInGrave, playerId, world.getName(), LocSign.getBlockX(), LocSign.getBlockY(), LocSign.getBlockZ());

                    DataBase.execute(executeFormatInGrave);

                    entities.stream().forEach((entity) -> entity.remove());

                    int lastGrave = getLastGrave(playerId);

                    itemSerializedContainers.stream().forEach((String Isc) -> {
                        String executeInItems = "insert into items (item, grave_id) values ('" + Isc + "', " + lastGrave + ");";
                        DataBase.execute(executeInItems);
                    });

                    try {
                        LocSign.setBlock(BlockTypes.WALL_SIGN.getDefaultState().with(Keys.DIRECTION, SOUTH).get());

                        TileEntity tile = (TileEntity) LocSign.getTileEntity().get();
                        Text line0 = Texts.of(TextColors.DARK_RED, "[grave]");
                        Text line1 = Texts.of(player.getName());
                        Text line2 = Texts.of(TextColors.DARK_GREEN, "#" + lastGrave);
                        dataofsign.setLines(tile, line0, line1, line2, null);

                        msgDied = Texts.of(TextColors.DARK_GREEN, "Your grave is in " + world.getName() + " | "
                                + LocSign.getBlockX() + " | "
                                + LocSign.getBlockY() + " | "
                                + LocSign.getBlockZ() + ".");
                        player.sendMessage(msgDied);
                    } catch (PositionOutOfBoundsException e) {
                        player.sendMessages(noGrave);
                    }

                }
            }
        } else {
        }
    }
}
