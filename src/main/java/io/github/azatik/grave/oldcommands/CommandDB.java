package io.github.azatik.grave.oldcommands;

import io.github.azatik.grave.database.DataBase;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

//This testing class

public class CommandDB implements CommandCallable {

    private final Game game;

    public CommandDB(Game game) {
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
                msg1 = Texts.of(TextColors.GREEN, "While The Start.");
                player.sendMessage(msg1);

                try {
                    ArrayList<Text> GetListTextRs = DataBase.GetListTextRs();
                    
                    player.sendMessages(GetListTextRs);

                
                } catch (SQLException ex) {
                    Logger.getLogger(CommandDB.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
                msg2 = Texts.of(TextColors.GREEN, "While The End.");
                player.sendMessage(msg2);
            }
            if ("writein".equals(args[0])) {
                String execute_players = "insert into players(uuid, name) values (1, 'tom'); "
                        + "insert into players(uuid, name) values (1, 'azat');"
                        + "insert into players(uuid, name) values (1, 'azalia');"
                        + "insert into players(uuid, name) values (1, 'milana');";
                
                String execute_graves = "insert into graves(player_id, world_name, coord_x , coord_y, coord_z) values (1, 'world', -256, 246, 102); " +
                                        "insert into graves(player_id, world_name, coord_x , coord_y, coord_z) values (1, 'aether', -258, 29, 100); " +
                                        "insert into graves(player_id, world_name, coord_x , coord_y, coord_z) values (2, 'tropicraft', -2568, 49, 1200); " +
                                        "insert into graves(player_id, world_name, coord_x , coord_y, coord_z) values (3, 'the_end', -2558, 39, 100); " +
                                        "insert into graves(player_id, world_name, coord_x , coord_y, coord_z) values (3, 'nether', 558, 49, -1002);";
                
                DataBase.execute(execute_players);
                DataBase.execute(execute_graves);
            }
            if ("format".equals(args[0])) {         
                String notformat = "Сегодня %s лето. Будет %s и %s";
                String arg1 = "наступит";
                String arg2 = "тепло";
                String arg3 = "хорошо";
                String format = String.format(notformat, arg1, arg2, arg3);
            }
            if ("item".equals(args[0])) {
                ItemStack item1 = game.getRegistry().createBuilder(ItemStack.Builder.class).itemType(ItemTypes.BEDROCK).build();
                DataContainer toContainer1 = item1.toContainer();
                
                ItemStack item2 = game.getRegistry().createBuilder(ItemStack.Builder.class).fromContainer(toContainer1).build();               
                DataContainer toContainer2 = item2.toContainer();
                
                Text msg3 = Texts.of(toContainer2);
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