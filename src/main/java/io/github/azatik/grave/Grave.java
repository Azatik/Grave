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
package io.github.azatik.grave;

import io.github.azatik.grave.commands.ConfigCmd;
import io.github.azatik.grave.commands.GraveCheckCmd;
import io.github.azatik.grave.commands.GraveCmd;
import io.github.azatik.grave.commands.GraveGetCmd;
import io.github.azatik.grave.commands.GraveHelpCmd;
import io.github.azatik.grave.commands.GraveShowCmd;
import io.github.azatik.grave.commands.LocaleCmd;
import io.github.azatik.grave.commands.TestCmd;
import io.github.azatik.grave.configuration.ConfigManagerConfig;
import io.github.azatik.grave.configuration.ConfigManagerMsg;
import org.spongepowered.api.plugin.Plugin;
import org.slf4j.Logger;
import io.github.azatik.grave.database.DataBase;
import io.github.azatik.grave.events.EventBreakGrave;
import io.github.azatik.grave.events.EventChangeSign;
import io.github.azatik.grave.events.EventDropItem;
import io.github.azatik.grave.messages.MsgKeys;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.inject.Inject;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.text.Text;

@Plugin(id = Grave.PLUGIN_ID, name = Grave.PLUGIN_NAME, version = Grave.PLUGIN_VERSION)
public class Grave {
    public static final String PLUGIN_ID = "grave", PLUGIN_NAME = "Grave", PLUGIN_VERSION = "0.1";    
    private static Grave instance;
    
    @Inject
    private Logger logger;
    @Inject
    private Game game;
    @Inject
    private EventManager eventManager;
    
    public static Grave getInstance() {
        return instance;
    }  
    public Logger getLogger() {
        return logger;
    }
    public Game getGame() {
        return game;
    }
    
    @Listener
    public void gamePreInit(GamePreInitializationEvent event) {
        instance = this;
    }
    
    @Listener
    public void gameInit(GameInitializationEvent event) throws IOException {
        logger.info("Grave Loading");
        
        try {
            DataBase.setup(game);
            logger.info("DataBase Loaded");
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(Grave.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        new ConfigManagerConfig();
        new ConfigManagerMsg();
        new MsgKeys();
        
        eventManager.registerListeners(this, new EventDropItem());
        eventManager.registerListeners(this, new EventBreakGrave());
        eventManager.registerListeners(this, new EventChangeSign());
        //eventManager.registerListeners(this, new EventExp());      
        //eventManager.registerListeners(this, new EventBreakProtectGrave());
        //eventManager.registerListeners(this, new EventDropSign());
        
        //***test events
        //eventManager.registerListeners(this, new EventDropItemCauseTest());
        //eventManager.registerListeners(this, new EventBreakBlock());
        
        //***old events
        //eventManager.registerListeners(this, new EventRightClickGrave()); 
        
        registerCommands();
        
        
    }

    @Listener
    public void gameStarting(GameStartingServerEvent event) {
        logger.info("Grave Loaded!");
    }

    @Listener
    public void gameStopping(GameStoppingServerEvent event) {
        logger.info("Grave Unloaded!");
    }
    
    private void registerCommands() {
        CommandSpec graveHelpCommand = CommandSpec.builder()
                .description(Text.of("Help command"))
                .permission("grave.help")
                .executor(new GraveHelpCmd())
                .build();
        
        CommandSpec graveShowCommand = CommandSpec.builder()
                .description(Text.of("This command show your graves"))
                .permission("grave.show")
                .arguments(GenericArguments.optional(GenericArguments.string(Text.of("player"))))
                .executor(new GraveShowCmd()).build();
        
        CommandSpec graveCheckCommand = CommandSpec.builder()
                .description(Text.of("This command checking graves"))
                .permission("grave.check")
                .arguments(GenericArguments.optional(GenericArguments.integer(Text.of("number"))))
                .executor(new GraveCheckCmd()).build();
        
        CommandSpec graveGetCommand = CommandSpec.builder()
                .description(Text.of("This command gives you the grave"))
                .permission("grave.get")
                .arguments(GenericArguments.optional(GenericArguments.integer(Text.of("number"))))
                .executor(new GraveGetCmd()).build();
        
        CommandSpec graveCommand = CommandSpec.builder()
                .description(Text.of("essentials command"))
                .permission("grave.command")
                .executor(new GraveCmd())
                .child(graveHelpCommand, "help")
                .child(graveShowCommand, "show")
                .child(graveCheckCommand, "check")
                .child(graveGetCommand, "get")
                .build();
        game.getCommandManager().register(this, graveCommand, "grave");
        
        CommandSpec testCommand = CommandSpec.builder()
                .description(Text.of("test command"))
                .permission("test.command")
                .executor(new TestCmd())
                .build();
        game.getCommandManager().register(this, testCommand, "test");
        
        CommandSpec configCommand = CommandSpec.builder()
                .description(Text.of("config command"))
                .permission("config.command")
                .executor(new ConfigCmd())
                .build();
        game.getCommandManager().register(this, configCommand, "config");
        
        CommandSpec localeCommand = CommandSpec.builder()
                .description(Text.of("locale command"))
                .permission("locale.command")
                .executor(new LocaleCmd())
                .build();
        game.getCommandManager().register(this, localeCommand, "locale");
    }
}
