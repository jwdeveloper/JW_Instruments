package jw.guitar.factory;

import jw.fluent_api.logger.OldLogger;
import jw.fluent_plugin.implementation.FluentAPI;
import jw.guitar.data.PluginPermissions;
import jw.guitar.gui.GuitarCraftingGui;
import jw.guitar.gui.songs.SongsListGui;
import jw.guitar.managers.InstrumentManager;
import jw.guitar.services.InstrumentService;
import jw.fluent_api.spigot.commands.FluentCommand;
import jw.fluent_api.spigot.commands.api.builder.CommandBuilder;
import jw.fluent_api.spigot.commands.api.builder.config.ArgumentConfig;
import jw.fluent_api.spigot.commands.api.enums.ArgumentDisplay;
import jw.fluent_api.spigot.commands.api.enums.ArgumentType;
import jw.fluent_api.spigot.commands.implementation.events.PlayerCommandEvent;
import jw.fluent_api.spigot.messages.FluentMessage;
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
                var gui = FluentAPI.spigot().playerContext().find(SongsListGui.class, event.getPlayer());
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
                OldLogger.log("Name", name);
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
                                var gui = FluentAPI.spigot().playerContext().find(GuitarCraftingGui.class, event.getPlayer());
                                gui.open(event.getPlayer());
                            });
                        });
        return cmd;
    }

}
