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
package io.github.azatik.grave.utils;

import org.spongepowered.api.data.property.block.MatterProperty;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class MatterCheck {
    public static boolean BlockIsSolid(Location<World> location){
        String matterName = location.getBlock().getProperty(MatterProperty.class).get().getValue().name();
        return "SOLID".equals(matterName);
    }
    public static boolean BlockIsLiquid(Location<World> location) {
        String matterName = location.getBlock().getProperty(MatterProperty.class).get().getValue().name();
        return "LIQUID".equals(matterName);
    } 
    public static boolean BlockIsGas(Location<World> location) {
        String matterName = location.getBlock().getProperty(MatterProperty.class).get().getValue().name();
        return "GAS".equals(matterName);
    }
}
