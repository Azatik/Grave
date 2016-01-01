package io.github.azatik.grave.oldcommands;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.title.Title;

//This testing class

public class CommandMsg implements CommandCallable {

    private final Game game;

    public CommandMsg(Game game) {
        this.game = game;
    }
    
    public Text msg1;
    public Text msg2;
    
    @Override
    public CommandResult process(CommandSource source, String arguments) throws CommandException {
        if (source instanceof Player) {
            final Player player = (Player) source;
            final String[] args = arguments.split(" ");
                
            if (arguments.isEmpty()) {
                Text emptyMsg = Text.of(TextColors.DARK_GREEN, "Empty Command");
                player.sendMessage(emptyMsg);
            }
            if ("title".equals(args[0])) {
                Text title = Text.of(TextColors.GOLD, "Wild Life");
                Text subTitle = Text.of(TextColors.GOLD, "Welcome aboard, " + player.getName());                
                player.sendTitle(Title.of(title, subTitle));
                
                try {
                    TimeUnit.SECONDS.sleep(15);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CommandMsg.class.getName()).log(Level.SEVERE, null, ex);
                }
                player.clearTitle();
            }
            if ("format".equals(args[0])) {         
                String notformat = "Сегодня %s лето. Будет %s и %s";
                String arg1 = "наступит";
                String arg2 = "тепло";
                String arg3 = "хорошо";
                String format = String.format(notformat, arg1, arg2, arg3);
            }
            if ("item".equals(args[0])) {
                ItemStack is = player.getItemInHand().get();
                DataContainer toContainer = is.toContainer();
                Text msg3 = Text.of(toContainer);
                player.sendMessage(msg3);
            }
        }

        return CommandResult.success();
    }

    @Override
    public List<String> getSuggestions(CommandSource cs, String string) throws CommandException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean testPermission(CommandSource cs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public java.util.Optional<? extends Text> getShortDescription(CommandSource cs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public java.util.Optional<? extends Text> getHelp(CommandSource cs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Text getUsage(CommandSource cs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}