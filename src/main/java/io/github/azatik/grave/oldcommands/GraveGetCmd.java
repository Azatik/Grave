package io.github.azatik.grave.oldcommands;

import io.github.azatik.grave.Grave;
import io.github.azatik.grave.database.DataBase;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

public class GraveGetCmd implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Game game = Grave.getInstance().getGame();
        Optional<String> graveNumber = args.getOne("Number");
        if (src instanceof Player) {
            Player player = (Player) src;
            if (graveNumber.isPresent()) {
                try {
                    Integer graveNumberInt = new Integer(graveNumber.get());
                    
                    DataBase.materializeItems(graveNumberInt, player.getLocation(), player);
                    
                    player.sendMessages(Texts.of(TextColors.GOLD, "Материализовалась могила #" + graveNumberInt));
                    return CommandResult.success();
                } catch (SQLException | IOException ex) {
                    Logger.getLogger(GraveGetCmdOne.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        } else if (src instanceof ConsoleSource) {
            src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /grave item!"));
        } else if (src instanceof CommandBlockSource) {
            src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /grave item!"));
        }
        return CommandResult.success();
    }
}
