package io.github.azatik.grave.oldevents;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import io.github.azatik.grave.utils.SignManipulator;
import java.util.ArrayList;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

public class EventInteractSign {
    //This testing Event
    
    Text msg;   
    SignManipulator dataofsign = new SignManipulator();
    
    @Listener
    public void onPlayerInteractBlock(InteractBlockEvent.Secondary event) {
        if (event.getCause().first(Player.class).isPresent()) {
            Player player = (Player) event.getCause().first(Player.class).get();
            if (event.getTargetBlock().getState().getType().equals(BlockTypes.WALL_SIGN)
                    || event.getTargetBlock().getState().getType().equals(BlockTypes.STANDING_SIGN)) {
                Location location = event.getTargetBlock().getLocation().get();
                TileEntity tile = (TileEntity) location.getTileEntity().get();
                ArrayList<String> lines = dataofsign.getLines(tile);
                
                msg = Texts.of(TextColors.GOLD, "Sign Lines:");
                player.sendMessage(msg);
                
                lines.stream().forEach((line) -> {
                    player.sendMessage(Texts.of(TextColors.WHITE, line));
                });
            }
        }
    }
}
