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
import io.github.azatik.grave.utils.SignManipulator;
import java.util.ArrayList;
import java.util.Optional;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.data.manipulator.immutable.tileentity.ImmutableSignData;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class EventChangeGrave {

    @Listener //(ignoreCancelled=false, order = BEFORE_POST)
    public void onChangeGrave(ChangeBlockEvent.Break event) {
        boolean causeOut = false;
        if (causeOut) {
            Grave.getInstance().getLogger().info(event.getCause().toString());
        }
        boolean playerCause = event.getCause().first(Player.class).isPresent();
        if (playerCause) {
            Player player = event.getCause().first(Player.class).get();

            event.getTransactions().stream().forEach((trans) -> {
                if (!trans.getOriginal().getState().getType().equals(BlockTypes.WALL_SIGN)) {

                    /*SignManipulator dataofsign = new SignManipulator();
                    Location<World> location = trans.getOriginal().getLocation().get();
                    TileEntity tileEntity = (TileEntity) location.getTileEntity().get();
                    ArrayList<String> signLines = dataofsign.getLines(tileEntity);
                    SignData signData = trans.getOriginal().getLocation().get().getOrCreate(SignData.class).get();
                    ArrayList<String> signLines = dataofsign.getLines(signData);
                    
                    Text line0 = Texts.of(TextColors.GRAY, signLines.get(0));                   
                    
                    player.sendMessages(line0);

                    if (signLines.get(0).equals("[grave]")) {
                        if (signLines.get(2).contains("#")) {
                            int IDGrave = Integer.parseInt(signLines.get(2).replace("#", ""));
                            Text msgID = Texts.of(TextColors.GRAY, "Code grave: " + IDGrave);
                            player.sendMessages(msgID);
                        }
                    }*/
                    Location<World> location = trans.getOriginal().getLocation().get();
                    Text msg = Texts.of(TextColors.GRAY, "You broke: " + location.getBlockType().getName());
                    player.sendMessages(msg);
                }
            });
        }
    }
}
