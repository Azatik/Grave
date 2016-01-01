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

import io.github.azatik.grave.Grave;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class GraveContainerCmd implements CommandExecutor {
    
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Game game = Grave.getInstance().getGame();
        if (src instanceof Player) {
            Player player = (Player) src;         
            ItemStack stack = game.getRegistry().createBuilder(ItemStack.Builder.class).build();
            
            String contSt = "MemoryDataContainer{map={ItemType=ironbackpacks:basicBackpack, Count=1, UnsafeDamage=0, UnsafeData=MemoryDataView{path=UnsafeData, map={Upgrades=[MemoryDataContainer{map={Upgrade=2}}], MostSigUUID=-1213882153550525462, LeastSigUUID=-8069292939825562437, Items=[MemoryDataContainer{map={Slot=0, id=minecraft:wool, Count=1, Damage=3}}, MemoryDataContainer{map={Slot=1, id=minecraft:wool, Count=1, Damage=2}}, MemoryDataContainer{map={Slot=2, id=minecraft:stone, Count=1, Damage=2}}]}}}}";
            //DataContainer cont = contSt;
            
            
            
            //stack.setRawData(cont);
            
        } else if (src instanceof ConsoleSource) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /grave item!"));
        } else if (src instanceof CommandBlockSource) {
            src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /grave item!"));
        }
        return CommandResult.success();
    }
}