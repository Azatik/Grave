package io.github.azatik.grave.oldcommands;

import org.spongepowered.api.Game;
import org.spongepowered.api.plugin.PluginContainer;

public class GraveRegisterCommands {
    private PluginContainer container;
    private Game game;

    /*public GraveRegisterCommands(PluginContainer pluginContainer) {
        super();
        container = pluginContainer;
        Grave plugin = (Grave) (container.getInstance() instanceof Grave ? container
                .getInstance() : null);
        game = plugin.getGame();
    }*/

    public void registerCmds() {

        // grave show [player]
        
        /*CommandSpec graveShow = CommandSpec
                .builder()
                .description(
                        Texts.of(" Показывает могилы игрока."))
                .permission("grave.graveshow")
                .arguments(
                        GenericArguments.optional(GenericArguments.string(
                                Texts.of("Player"))))
                .executor(new CmdGraveShow(container)).build();*/

        /*// gy nearest [World] [x, y, z]
        CommandSpec graveyardNearestCommand = CommandSpec
                .builder()
                .description(
                        Texts.of("Identifies the nearest graveyard from the provided world and location or your current world and location if neither is provided."))
                .permission("graveyards.command.nearest")
                .arguments(
                        GenericArguments.optional(GenericArguments.world(
                                Texts.of("World"), game)),
                        GenericArguments.optional(GenericArguments
                                .vector3d(Texts.of("Location"))))
                .executor(new GraveyardNearestExecutor(container)).build();

        // gy create <Name> [World] [x, y, z]
        CommandSpec graveyardCreateCommand = CommandSpec
                .builder()
                .description(
                        Texts.of("Creates a graveyard with the given name at the provided world and location or your current world and location if neither is provided."))
                .permission("graveyards.command.create")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Texts
                                .of("Name"))),
                        GenericArguments.optional(GenericArguments.world(
                                Texts.of("World"), game)),
                        GenericArguments.optional(GenericArguments
                                .vector3d(Texts.of("Location"))))
                .executor(new GraveyardCreateExecutor(container)).build();
        // gy destroy <Name> [World]
        CommandSpec graveyardDestroyCommand = CommandSpec
                .builder()
                .description(
                        Texts.of("Destroys a graveyard with the given name in the provided world or your current world if none is provided."))
                .permission("graveyards.command.destroy")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Texts
                                .of("Name"))),
                        GenericArguments.optional(GenericArguments.world(
                                Texts.of("World"), game)))
                .executor(new GraveyardDestroyExecutor(container)).build();
        // gy list [World]
        CommandSpec graveyardListCommand = CommandSpec
                .builder()
                .description(
                        Texts.of("Lists all graveyards in the provided world or your current world if none is provided."))
                .permission("graveyards.command.list")
                .arguments(
                        GenericArguments.optional(GenericArguments
                                .onlyOne(GenericArguments.world(
                                        Texts.of("World"), game))))
                .executor(new GraveyardListExecutor(container)).build();
        // gy*/
        
        /*CommandSpec graveyardCommand = CommandSpec.builder()
                .description(Texts.of("/grave [show|soon|soon]"))
                .permission("grave.help")
                .executor(new CmdGrave(container))
                .child(graveShow, "show")
                .build();
                game.getCommandDispatcher().register(container.getInstance(),
                graveyardCommand, "grave");*/
    }
}
