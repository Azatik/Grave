package io.github.azatik.grave.utils;

import org.spongepowered.api.data.property.block.MatterProperty;
import org.spongepowered.api.world.Location;

public class MatterCheck {
    public static boolean BlockIsSolid(Location location){
        String matterName = location.getBlock().getProperty(MatterProperty.class).get().getValue().name();
        return "SOLID".equals(matterName);
    }
    public static boolean BlockIsLiquid(Location location) {
        String matterName = location.getBlock().getProperty(MatterProperty.class).get().getValue().name();
        return "LIQUID".equals(matterName);
    } 
    public static boolean BlockIsGas(Location location) {
        String matterName = location.getBlock().getProperty(MatterProperty.class).get().getValue().name();
        return "GAS".equals(matterName);
    }
}
