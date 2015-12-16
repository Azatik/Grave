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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.azatik.grave.Grave;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
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
import org.spongepowered.api.data.translator.ConfigurateTranslator;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

public class JsonCmd implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Game game = Grave.getInstance().getGame();
        if (src instanceof Player) {
            try {
                //Writer
                Player player = (Player) src;
                ItemStackSnapshot itemSnapshot = player.getItemInHand().get().createSnapshot();
                DataContainer dataContainer = itemSnapshot.toContainer();
                
                Text msg1ContainerName = Texts.of(TextColors.YELLOW, "First container: " + dataContainer.toString());
                player.sendMessage(msg1ContainerName);
                
                StringWriter writer = new StringWriter();
                HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setSink(new Callable<BufferedWriter>() {
                    @Override
                    public BufferedWriter call() {
                        return new BufferedWriter(writer);
                    }
                }).build();               
                loader.save(ConfigurateTranslator.instance().translateData(dataContainer));
                
                //String in DataBase
                String stringDB = writer.toString();
                Text stringDBMsg = Texts.of(TextColors.YELLOW, "String DB: " + stringDB);
                player.sendMessage(stringDBMsg);
                ArrayList<String> one = new ArrayList();
                one.add(stringDB);
                one.add(stringDB);
                
                Gson gson = new GsonBuilder().create();
                String toJson = gson.toJson(one);
                Text toJsonMsg = Texts.of(TextColors.AQUA, "Serialize: " + toJson);
                player.sendMessage(toJsonMsg);
                
                Type type = new TypeToken<List<String>>() {
                }.getType();
                ArrayList<String> desOne = gson.fromJson(toJson,type);
                Text fromJsonMsg = Texts.of(TextColors.AQUA, "Deserialize: " + desOne.get(0));
                player.sendMessage(fromJsonMsg);
                //Reader
                StringReader reader = new StringReader(stringDB);
                DataView view = ConfigurateTranslator.instance().translateFrom(HoconConfigurationLoader.builder().setSource(new Callable<BufferedReader>() {
                    @Override
                    public BufferedReader call() {
                        return new BufferedReader(reader);
                    }
                }).build().load());
                
                DataContainer container = view.getContainer();
                Text msg2ContainerName = Texts.of(TextColors.YELLOW, "Second container: " + container.toString());
                player.sendMessage(msg2ContainerName);
            } catch (IOException ex) {
                Logger.getLogger(JsonCmd.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        } else if (src instanceof ConsoleSource) {
            src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /grave item!"));
        } else if (src instanceof CommandBlockSource) {
            src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /grave item!"));
        }
        return CommandResult.success();
    }
}