package jw.instruments.core.factory;

import jw.fluent_plugin.implementation.FluentApi;
import jw.fluent_plugin.implementation.modules.logger.FluentLogger;
import jw.instruments.core.data.PluginPermissions;
import jw.instruments.core.managers.InstrumentManager;
import jw.instruments.core.services.InstrumentService;
import jw.instruments.spigot.gui.GuitarCraftingGui;
import jw.instruments.spigot.gui.songs.SongsListGui;
import jw.fluent_api.spigot.commands.FluentCommand;
import jw.fluent_api.spigot.commands.api.builder.CommandBuilder;
import jw.fluent_api.spigot.commands.api.builder.config.ArgumentConfig;
import jw.fluent_api.spigot.commands.api.enums.ArgumentDisplay;
import jw.fluent_api.spigot.commands.api.enums.ArgumentType;
import jw.fluent_api.spigot.commands.implementation.events.PlayerCommandEvent;
import jw.fluent_api.spigot.messages.FluentMessage;
import org.bukkit.ChatColor;

public class CommandFactory {

    public static void instumentUI(InstrumentManager manager, PlayerCommandEvent event) {
        final var player = event.getPlayer();
        if (!manager.validatePlayer(player)) {
            return;
        }
        manager.get(player).openGUI();
    }

    public static CommandBuilder songsListCmd() {

        var cmd = FluentCommand.create("songs");
        cmd.propertiesConfig(propertiesConfig ->
        {
            propertiesConfig.addPermissions(PluginPermissions.SONGS_CMD);
        });

        cmd.eventsConfig(eventConfig ->
        {
            eventConfig.onPlayerExecute(event ->
            {
                var gui = FluentApi.spigot().playerContext().find(SongsListGui.class, event.getPlayer());
                gui.open(event.getPlayer());
            });
        });
        return cmd;
    }

    public static CommandBuilder getInstrumentCmd(InstrumentService instumentService) {
        var cmd = FluentCommand.create("get");
        cmd.propertiesConfig(propertiesConfig ->
        {
            propertiesConfig.addPermissions(PluginPermissions.GET_CMD);
        });

        cmd.argumentsConfig(argumentConfig ->
        {
            createGuitarArgument(argumentConfig, instumentService);
        });
        cmd.eventsConfig(eventConfig ->
        {
            eventConfig.onPlayerExecute(event ->
            {
                var name = event.getArgs()[1];
                FluentApi.logger().log("Name", name);
                var guitar = instumentService.getByName(name);
                if (guitar.isEmpty()) {
                    FluentMessage.message()
                            .color(ChatColor.RED)
                            .text("Guitar ")
                            .color(ChatColor.GRAY)
                            .text(name)
                            .color(ChatColor.RED)
                            .text(" not found ")
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
                                var gui = FluentApi.spigot().playerContext().find(GuitarCraftingGui.class, event.getPlayer());
                                gui.open(event.getPlayer());
                            });
                        });
        return cmd;
    }

}
