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
package io.github.azatik.grave.commands;

import io.github.azatik.grave.database.DataBase;
import io.github.azatik.grave.events.EventBreakGrave;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class GraveGetCmd implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player) {
            Optional<Integer> numberArg = args.<Integer>getOne("number");
            Player player = (Player) src;
            if (numberArg.isPresent()) {
                try {
                    int graveNumber = numberArg.get();
                    DataBase.materializeItems(graveNumber, player.getLocation(), player);                   
                    return CommandResult.success();
                } catch (SQLException ex) {
                    player.sendMessage(Text.of(TextColors.RED, "Ошибка базы данных!!!"));
                    Logger.getLogger(EventBreakGrave.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(EventBreakGrave.class.getName()).log(Level.SEVERE, null, ex);
                    player.sendMessage(Text.of(TextColors.RED, "Данные о могиле повреждены!!!"));
                }
            } else {
                player.sendMessage(Text.of(TextColors.RED, "Please, enter argument: /grave get [#grave]"));
                return CommandResult.success();
            }
        } else if (src instanceof ConsoleSource) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /grave show!"));
        } else if (src instanceof CommandBlockSource) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /grave show!"));
        }
        return CommandResult.empty();
    }
}
