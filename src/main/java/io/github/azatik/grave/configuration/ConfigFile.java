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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class ConfigFile {
    public static final File file = new File("mods/grave/grave.conf");
    public static final ConfigurationLoader<CommentedConfigurationNode> cLoader = HoconConfigurationLoader.builder().setFile(file).build();
    public static ConfigurationNode config = cLoader.createEmptyNode(ConfigurationOptions.defaults());
    
    public static ArrayList<String> notGraveWorlds = null;
    
    public static void setup() {

		try {
			
			if (!file.exists()) {
				
				file.createNewFile();
                                
                                
                                
				String world = "world";				
				config.getNode("config", "worlds", "world").setValue(world);
                                
		        cLoader.save(config);
				
			}
			
			config = cLoader.load();
			
		     
		} catch (IOException e) {}
		
	}
	
	public static List<? extends ConfigurationNode> MESSAGE() { 
            return config.getNode("config", "worlds", "world").getChildrenList();
        
        }		
}
