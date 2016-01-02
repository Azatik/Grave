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
package io.github.azatik.grave.configuration;

import io.github.azatik.grave.Grave;
import java.io.File;
import java.io.IOException;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class ConfigManagerConfig {
    private File file;
    private CommentedConfigurationNode config;
    private ConfigurationLoader<CommentedConfigurationNode> loader;
    
    public ConfigManagerConfig() {
        String folder = "config/" + Grave.PLUGIN_NAME + "/";
        if (!new File(folder).isDirectory()) {
            new File(folder).mkdirs();
        }
        file = new File(folder, "config.conf");

        create();
        load();
        initConfig();
    }

    public ConfigurationLoader<CommentedConfigurationNode> getLoader() {
        return loader;
    }

    public CommentedConfigurationNode getConfig() {
        return config;
    }
    
    public void save() {
        try {
            loader.save(config);
        } catch (IOException e) {
            Grave.getInstance().getLogger().error("Failed to save config");
        }
    }
    
    private void initConfig() {
        if (file.getName().equalsIgnoreCase("config.conf")) {
            if (config.getNode("Grave", "Message").getString() == null) {
                config.getNode("Grave", "Message").setValue("New Year 2016!").setComment("Just Test Message");
            }
            if (config.getNode("Grave", "Love").getString() == null) {
                config.getNode("Grave", "Love").setValue("My love = X").setComment("Just Second Test Message");
            }
        }
        save();
    }

    public void create() {
        if (!file.exists()) {
            try {
                Grave.getInstance().getLogger().info("Creating new " + file.getName() + " file...");
                file.createNewFile();
            } catch (IOException e) {
                Grave.getInstance().getLogger().error("Failed to create new config file");
            }
        }
    }
    
    public void load() {
        loader = HoconConfigurationLoader.builder().setFile(file).build();
        try {
            config = loader.load();
        } catch (IOException e) {
            Grave.getInstance().getLogger().error("Failed to load config");
        }
    }
}
