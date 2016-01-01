package io.github.azatik.grave.utils;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;

public class LowerLocation {

    public Location SearchLowerLocation(Location location, Player player) {
        Boolean debug = false;
        Location LowerLocation = location;              
        BlockType blockType = LowerLocation.getBlockType();   
        int x = LowerLocation.getBlockX();
        int y = LowerLocation.getBlockY();
        int z = LowerLocation.getBlockZ();        
        Text start = Text.of("Start while");       
        Text stop = Text.of("Stop while");
        
        if (debug == true) player.sendMessage(start);
        while (blockType == BlockTypes.AIR) {
            y--;
            Text iter = Text.of("While iter: " + y);
            if (debug == true) player.sendMessage(iter);
            LowerLocation = new Location(player.getWorld(), x, y, z);
            blockType = LowerLocation.getBlockType();
        }
        LowerLocation = new Location(player.getWorld(), x, y+1, z);
        if (debug == true) player.sendMessage(stop);
        return LowerLocation;
    }
}
