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

import io.github.azatik.grave.Grave;
import static io.github.azatik.grave.database.DataBase.datasource;
import io.github.azatik.grave.utils.SignManipulator;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.TileEntity;
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
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class GraveCheckCmd implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player) {
            Optional<Integer> numberArg = args.<Integer>getOne("number");
            Player player = (Player) src;
            if (numberArg.isPresent()) {
                try {
                    Connection connection = datasource.getConnection();
                    Statement statement = connection.createStatement();
                    int graveNumber = numberArg.get();
                    String executeGetGrave = "select * from graves where id = " + graveNumber;
                    ResultSet rsGetGrave = statement.executeQuery(executeGetGrave);
                    
                    
                    rsGetGrave.first();

                    String graveWorldName = rsGetGrave.getString("world_name");
                    int graveX = rsGetGrave.getInt("coord_x");
                    int graveY = rsGetGrave.getInt("coord_y");
                    int graveZ = rsGetGrave.getInt("coord_z");
                    
                    Game game = Grave.getInstance().getGame();

                    if (game.getServer().getWorld(graveWorldName).isPresent()) {
                        World world = game.getServer().getWorld(graveWorldName).get();
                        Location<World> location = new Location<>(world, graveX, graveY, graveZ);

                        if (location.getBlockType().equals(BlockTypes.WALL_SIGN) || location.getBlockType().equals(BlockTypes.STANDING_SIGN)) {
                            TileEntity sign = location.getTileEntity().get();
                            SignManipulator signManipulator = new SignManipulator();
                            ArrayList<String> lines = signManipulator.getLines(sign);

                            if (lines.get(0).equals("[grave]") && lines.get(2).contains("#")) {
                                player.sendMessage(Text.of(TextColors.GREEN, "Могила существует в мире"));
                            } else {
                                player.sendMessage(Text.of(TextColors.RED, "Могила НЕ существует в мире"));
                            }
                        } else {
                            player.sendMessage(Text.of(TextColors.RED, "Могила НЕ существует в мире"));
                        }
                    } else {
                        player.sendMessage(Text.of(TextColors.RED, "Мира, в котором существовала могила, больше нет!!!"));
                    }
                    return CommandResult.success();
                } catch (SQLException ex) {
                    player.sendMessage(Text.of(TextColors.RED, "Такой могилы не существует!!!"));
                }
                return CommandResult.success();
            } else {
                player.sendMessage(Text.of(TextColors.RED, "Please, enter argument: /grave check [#grave]"));
                return CommandResult.success();
            }
        } else if (src instanceof ConsoleSource) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /grave show!"));
        } else if (src instanceof CommandBlockSource) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /grave show!"));
        }
        return CommandResult.success();
    }
}
