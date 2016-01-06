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
import io.github.azatik.grave.utils.SignManipulator;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.manipulator.immutable.tileentity.ImmutableSignData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import static org.spongepowered.api.event.Order.BEFORE_POST;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.Tristate;

public class EventBreakGrave {

    @Listener (order = BEFORE_POST)
    @IsCancelled(Tristate.UNDEFINED)
    public void onBreakGrave(ChangeBlockEvent.Break event) {
        boolean causeOut = false;
        if (causeOut) {
            Grave.getInstance().getLogger().info(event.getCause().toString());
        }
        boolean playerCause = event.getCause().first(Player.class).isPresent();
        if (playerCause) {
            Player player = event.getCause().first(Player.class).get();

            event.getTransactions().stream().forEach((trans) -> {
                if (trans.getOriginal().getState().getType().equals(BlockTypes.WALL_SIGN)
                        || trans.getOriginal().getState().getType().equals(BlockTypes.STANDING_SIGN)) {

                    SignManipulator dataofsign = new SignManipulator();

                    ImmutableSignData iSignData = trans.getOriginal().get(ImmutableSignData.class).get();
                    ArrayList<String> signLines = dataofsign.getLines(iSignData);

                    if (signLines.get(0).equals("[grave]")) {
                        if (signLines.get(2).contains("#")) {                           
                            try {
                                int IDGrave = Integer.parseInt(signLines.get(2).replace("#", ""));
                                DataBase.materializeItems(IDGrave, trans.getOriginal().getLocation().get(), player);
                                Text msgID = Text.of(TextColors.GRAY, "Materialize grave #" + IDGrave);
                                player.sendMessages(msgID);
                                event.setCancelled(false);
                            } catch (SQLException ex) {
                                Logger.getLogger(EventBreakGrave.class.getName()).log(Level.SEVERE, null, ex);
                                player.sendMessage(Text.of(TextColors.RED, "Ошибка базы данных!!!"));
                            } catch (IOException ex) {
                                Logger.getLogger(EventBreakGrave.class.getName()).log(Level.SEVERE, null, ex);
                                player.sendMessage(Text.of(TextColors.RED, "Данные о могиле повреждены!!!"));
                            }
                        }
                    }
                }
            });
        }
    }
}
