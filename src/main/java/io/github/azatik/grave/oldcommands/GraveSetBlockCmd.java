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
package io.github.azatik.grave.oldcommands;

import io.github.azatik.grave.utils.SignManipulator;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import static org.spongepowered.api.util.Direction.SOUTH;
import org.spongepowered.api.util.PositionOutOfBoundsException;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class GraveSetBlockCmd implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player) {
            SignManipulator dataofsign = new SignManipulator();
            Player player = (Player) src;
            World world = player.getWorld();
            
            Location<World> location = player.getLocation();
            Location<World> location2 = new Location(world,
                    location.getBlockX(),
                    location.getBlockY(),
                    location.getBlockZ() + 1);
            try {
                location.setBlockType(BlockTypes.STONE);
                location2.setBlock(BlockTypes.WALL_SIGN.getDefaultState().with(Keys.DIRECTION, SOUTH).get());
                Text msgPlayer = Texts.of(TextColors.DARK_GREEN, "Block set");
                player.sendMessage(msgPlayer);
                
                TileEntity tile = (TileEntity) location2.getTileEntity().get();
                    Text line0 = Texts.of(TextColors.DARK_RED, "[Grave]");
                    Text line1 = Texts.of(player.getName());
                    Text line2 = Texts.of(TextColors.DARK_GREEN, "#x");
                    dataofsign.setLines(tile, line0, line1, line2, null);
                
            } catch (PositionOutOfBoundsException e) {
                Text msgPlayer2 = Texts.of(TextColors.DARK_RED, "Block NOT set");
                player.sendMessages(msgPlayer2);
            }
            
        } else if (src instanceof ConsoleSource) {
            src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /grave item!"));
        } else if (src instanceof CommandBlockSource) {
            src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /grave item!"));
        }
        return CommandResult.success();
    }
}
