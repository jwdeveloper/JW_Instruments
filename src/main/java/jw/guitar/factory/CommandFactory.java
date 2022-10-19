package jw.guitar.factory;

import jw.guitar.data.PluginPermissions;
import jw.guitar.gui.GuitarCraftingGui;
import jw.guitar.gui.songs.SongsListGui;
import jw.guitar.managers.InstrumentManager;
import jw.guitar.services.InstrumentService;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.FluentInjection;
import jw.spigot_fluent_api.fluent_commands.FluentCommand;
import jw.spigot_fluent_api.fluent_commands.api.builder.CommandBuilder;
import jw.spigot_fluent_api.fluent_commands.api.builder.config.ArgumentConfig;
import jw.spigot_fluent_api.fluent_commands.api.enums.ArgumentDisplay;
import jw.spigot_fluent_api.fluent_commands.api.enums.ArgumentType;
import jw.spigot_fluent_api.fluent_commands.implementation.events.PlayerCommandEvent;
import jw.spigot_fluent_api.fluent_logger.FluentLogger;
import jw.spigot_fluent_api.fluent_message.FluentMessage;
import org.bukkit.ChatColor;

public class CommandFactory {


    public static void guitarUiCmd(InstrumentManager manager, PlayerCommandEvent event) {
        final var player = event.getPlayer();
        if (!manager.validatePlayer(player)) {
            return;
        }
        manager.get(player).openGUI();
    }

    public static CommandBuilder songsCmd() {

        var cmd = FluentCommand.create("songs");
        cmd.propertiesConfig(propertiesConfig ->
        {
            propertiesConfig.addPermissions(PluginPermissions.SONGS_CMD);
        });

        cmd.eventsConfig(eventConfig ->
        {
            eventConfig.onPlayerExecute(event ->
            {
                var gui = FluentInjection.findPlayerInjection(SongsListGui.class, event.getPlayer());
                gui.open(event.getPlayer());
            });
        });
        return cmd;
    }

    public static CommandBuilder getCmd(InstrumentService guitarService) {
        var cmd = FluentCommand.create("get");
        cmd.propertiesConfig(propertiesConfig ->
        {
            propertiesConfig.addPermissions(PluginPermissions.GET_CMD);
        });

        cmd.argumentsConfig(argumentConfig ->
        {
            createGuitarArgument(argumentConfig, guitarService);
        });
        cmd.eventsConfig(eventConfig ->
        {
            eventConfig.onPlayerExecute(event ->
            {
                var name = event.getArgs()[1];
                FluentLogger.log("Name", name);
                var guitar = guitarService.getByName(name);
                if (guitar.isEmpty()) {
                    FluentMessage.message()
                            .color(ChatColor.RED)
                            .text("Guitar")
                            .color(ChatColor.GRAY)
                            .text(name)
                            .color(ChatColor.RED)
                            .text("not found")
                            .send(event.getPlayer());
                    return;
                }
                event.getPlayer().getInventory().setItem(0, guitar.get().getCustomModel().getItemStack());
            });
        });
        return cmd;
    }


    private static void createGuitarArgument(ArgumentConfig config, InstrumentService guitarService) {
        config.addArgument("guitar-type", argumentBuilder ->
        {
            argumentBuilder.setType(ArgumentType.CUSTOM);
            argumentBuilder.setTabComplete(guitarService.getNames());
            argumentBuilder.setArgumentDisplay(ArgumentDisplay.TAB_COMPLETE);
            argumentBuilder.setDescription("select guitar type");
        });
    }

    public static CommandBuilder craftingCmd() {
        var cmd = FluentCommand.create("crafting")
                        .eventsConfig(eventConfig ->
                        {
                            eventConfig.onPlayerExecute(event ->
                            {
                                var gui = FluentInjection.findPlayerInjection(GuitarCraftingGui.class, event.getPlayer());
                                gui.open(event.getPlayer());
                            });
                        });
        return cmd;
    }

}
