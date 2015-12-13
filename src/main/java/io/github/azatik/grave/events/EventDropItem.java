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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.IndirectEntityDamageSource;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.PositionOutOfBoundsException;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class EventDropItem {

    Text msgDied;
    Text noGrave = Texts.of(TextColors.DARK_RED, "Могила не обзовалась по какой-то причине." + "\n" + "Ты можешь её восстановить командой /grave repair [# могилы]");
    SignManipulator dataofsign = new SignManipulator();

    @Listener
    public void onDropItem(DropItemEvent.Destruct event) throws SQLException, IOException {
        /*Grave.getInstance().getLogger().info(event.getCause().toString());
         IndirectEntityDamageSource ieds = event.getCause().first(IndirectEntityDamageSource.class).get();
         Grave.getInstance().getLogger().info("Entity Type: " + ieds.getIndirectSource().getType().getName());*/
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
                Player player = event.getCause().first(Player.class).get();
                World world = player.getWorld();

                Location LocGranite = player.getLocation();

                //Here is the logic of setting the grave.
                Location LocSign = new Location(world,
                        LocGranite.getBlockX(),
                        LocGranite.getBlockY() + 1,
                        LocGranite.getBlockZ());

                List<Entity> entities = event.getEntities();
                ArrayList<String> itemContainers = new ArrayList();

                entities.stream().forEach((Entity entity) -> {
                    itemContainers.add(entity.getValue(Keys.REPRESENTED_ITEM).get().get().toContainer().toString());
                });

                int playerId = DataBase.getPlayerIdInGrave(player);
                String serializeItemContainers = serializeMassiveContainersItems(itemContainers);

                String executeInGrave = ("insert into graves(player_id, world_name, coord_x, coord_y, coord_z, items) values (%s, '%s', %s, %s, %s, '%s');");
                String executeFormatInGrave = String.format(executeInGrave, playerId, world.getName(), LocSign.getBlockX(), LocSign.getBlockY(), LocSign.getBlockZ(), serializeItemContainers);
                DataBase.execute(executeFormatInGrave);

                entities.stream().forEach((entity) -> entity.remove());

                int lastGrave = getLastGrave(playerId);

                try {
                    LocGranite.setBlockType(BlockTypes.STONE);
                    LocSign.setBlockType(BlockTypes.STANDING_SIGN);

                    TileEntity tile = (TileEntity) LocSign.getTileEntity().get();
                    Text line0 = Texts.of(TextColors.DARK_RED, "[Grave]");
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
        } else {
        }
    }

    String serializeMassiveContainersItems(List<String> list) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(list);
        oos.flush();
        baos.flush();
        oos.close();
        baos.close();
        return baos.toString();
    }
}
