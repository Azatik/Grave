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

import io.github.azatik.grave.database.DataBase;
import io.github.azatik.grave.utils.SignManipulator;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class EventRightClickGrave {

    @Listener
    public void onPlayerInteractSign(InteractBlockEvent.Secondary event) throws SQLException, IOException {
        if (event.getCause().first(Player.class).isPresent()) {
            Player player = (Player) event.getCause().first(Player.class).get();
            if (event.getTargetBlock().getState().getType().equals(BlockTypes.WALL_SIGN)) {
                Location location = event.getTargetBlock().getLocation().get();
                TileEntity tileEntity = (TileEntity) location.getTileEntity().get();
                SignManipulator dataofsign = new SignManipulator();
                ArrayList<String> signLines = dataofsign.getLines(tileEntity);

                if (signLines.get(0).equals("[grave]")) {
                    if (signLines.get(2).contains("#")) {
                        int IDGrave = Integer.parseInt(signLines.get(2).replace("#", ""));
                        DataBase.materializeItems(IDGrave, location, player);

                        World world = player.getWorld();
                        Location locationStone = new Location(world,
                                location.getBlockX(),
                                location.getBlockY(),
                                location.getBlockZ() - 1);
                        location.setBlockType(BlockTypes.AIR);
                        locationStone.setBlockType(BlockTypes.AIR);

                        Text msgID = Texts.of(TextColors.GRAY, "Материализовалась могила #" + IDGrave);
                        player.sendMessages(msgID);
                    }
                }
            }
        }
    }
}
