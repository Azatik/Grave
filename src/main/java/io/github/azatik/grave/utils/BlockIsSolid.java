package io.github.azatik.grave.utils;

import org.spongepowered.api.data.property.block.MatterProperty;
import org.spongepowered.api.world.Location;

public class BlockIsSolid {
    public boolean BlockIsSolid(Location location){
        String matterName = location.getBlock().getProperty(MatterProperty.class).get().getValue().name();
        return "SOLID".equals(matterName);
    }
}
