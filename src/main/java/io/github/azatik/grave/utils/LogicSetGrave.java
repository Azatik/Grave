/*
 * This file is part of SpongeAPI, licensed under the MIT License (MIT).
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

import static io.github.azatik.grave.utils.MatterCheck.BlockIsGas;
import static io.github.azatik.grave.utils.MatterCheck.BlockIsLiquid;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class LogicSetGrave {

    public static LogicClass setGrave(Location location, World world) {
        LogicClass loc = new LogicClass();

        int locationY = location.getBlockY();

        if ((locationY > 0)) {
            boolean logicStart = true;

            if (locationY > 255) {
                location = new Location(world, location.getBlockX(), 255, location.getBlockZ());
                if (!location.getBlockType().equals(BlockTypes.AIR)) {
                    logicStart = false;
                }
            }

            if (logicStart) {
                Location locationSurface = new Location(world, location.getBlockX(), location.getBlockY() - 1, location.getBlockZ());

                if (!conditionFly(locationSurface)) {
                    loc.setLoc(location);

                    if (BlockIsLiquid(location) && BlockIsLiquid(locationSurface)) {
                        loc.setBlockTypeSet(BlockTypes.WALL_SIGN);
                    } else {
                        loc.setBlockTypeSet(BlockTypes.STANDING_SIGN);
                    }

                }

                if (conditionFly(locationSurface)) {
                    int y = locationSurface.getBlockY();
                    boolean aboveVoid = false;
                    while (conditionFly(locationSurface)) {
                        y--;
                        locationSurface = new Location(world, location.getBlockX(), y, location.getBlockZ());
                        
                        if (y <= 0){
                            aboveVoid = true;
                            break;
                        }                      
                    }
                    if (!aboveVoid) {

                        boolean surfaceLiquid = BlockIsLiquid(locationSurface);
                        locationSurface = new Location(world, location.getBlockX(), y + 1, location.getBlockZ());
                        loc.setLoc(locationSurface);
                        if (surfaceLiquid) {
                            loc.setBlockTypeSet(BlockTypes.WALL_SIGN);
                        } else {
                            loc.setBlockTypeSet(BlockTypes.STANDING_SIGN);
                        }
                    } else {
                        loc.setLoc(location);
                        loc.setBlockTypeSet(BlockTypes.WALL_SIGN);
                    }
                }
            } else {
                loc.setLoc(location);
                loc.setBlockTypeSet(null);
            }
        } else {
            loc.setLoc(location);
            loc.setBlockTypeSet(null);
        }

        return loc;
    }

    private static boolean conditionFly(Location location) {
        return (location.getBlockType().equals(BlockTypes.AIR)) || (BlockIsGas(location));
    }
}
