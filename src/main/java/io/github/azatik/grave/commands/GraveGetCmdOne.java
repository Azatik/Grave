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
import static io.github.azatik.grave.database.DataBase.getItem;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

public class GraveGetCmdOne implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Game game = Grave.getInstance().getGame();
        Optional<String> graveNumber = args.getOne("Number");
        if (src instanceof Player) {
            Player player = (Player) src;
            if (graveNumber.isPresent()) {
                try {
                    Integer graveNumberInt = new Integer(graveNumber.get());
                    DataView itemDb = getItem(graveNumberInt);

                    /*items.stream().forEach((DataView item) -> {
                    Text output = Texts.of(item.getContainer().toString());
                    player.sendMessages(output);
                    });*/
                    //DataContainer container = items.get(0).getContainer();
                    
                    ItemStack stack = game.getRegistry().createBuilder(ItemStack.Builder.class).fromContainer(itemDb).build();
                    
                    Optional<Entity> optItem = player.getLocation().getExtent().createEntity(EntityTypes.ITEM, player.getLocation().getPosition());
                    if (optItem.isPresent()) {
                        Item item = (Item) optItem.get();
                        item.offer(Keys.REPRESENTED_ITEM, stack.createSnapshot());
                        player.getWorld().spawnEntity(item, Cause.of(player));
                    }
                    
                    player.sendMessages(Texts.of(TextColors.GOLD, "Вы получили предмет - " + stack.getItem().getName()));
                    return CommandResult.success();
                } catch (SQLException | IOException ex) {
                    Logger.getLogger(GraveGetCmdOne.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        } else if (src instanceof ConsoleSource) {
            src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /grave item!"));
        } else if (src instanceof CommandBlockSource) {
            src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /grave item!"));
        }
        return CommandResult.success();
    }
}
