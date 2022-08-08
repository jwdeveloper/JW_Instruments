package jw.guitar.factory;

import jw.guitar.data.PluginPermissions;
import jw.guitar.gameobjects.InstrumentPlayer;
import jw.guitar.gui.songs.SongsListGui;
import jw.guitar.services.InstrumentService;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.FluentInjection;
import jw.spigot_fluent_api.fluent_commands.FluentCommand;
import jw.spigot_fluent_api.fluent_commands.api.builder.CommandBuilder;
import jw.spigot_fluent_api.fluent_commands.api.builder.config.ArgumentConfig;
import jw.spigot_fluent_api.fluent_commands.api.enums.ArgumentDisplay;
import jw.spigot_fluent_api.fluent_commands.api.enums.ArgumentType;
import jw.spigot_fluent_api.fluent_gameobjects.implementation.GameObjectManager;
import jw.spigot_fluent_api.fluent_logger.FluentLogger;
import jw.spigot_fluent_api.fluent_message.FluentMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class CommandFactory {

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
               var gui = FluentInjection.getPlayerInjection(SongsListGui.class,event.getPlayer());
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
            createGuitarArgument(argumentConfig,guitarService);
        });
        cmd.eventsConfig(eventConfig ->
        {
            eventConfig.onPlayerExecute(event ->
            {
                var name = event.getArgs()[1];
                FluentLogger.log("Name", name);
                var guitar = guitarService.getByName(name);
                if(guitar.isEmpty())
                {
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
                event.getPlayer().getInventory().setItem(0,guitar.get().getCustomModel().getItemStack());
            });
        });
        return cmd;
    }

    public static CommandBuilder giveCmd(InstrumentService guitarService) {

        var cmd = FluentCommand.create("give");
        cmd.propertiesConfig(propertiesConfig ->
        {
            propertiesConfig.addPermissions(PluginPermissions.GIVE_CMD);
        });

        cmd.argumentsConfig(argumentConfig ->
        {
            createGuitarArgument(argumentConfig,guitarService);
            argumentConfig.addArgument("player", argumentBuilder -> {
                argumentBuilder.setType(ArgumentType.PLAYERS);
            });
        });
        cmd.eventsConfig(eventConfig ->
        {
            eventConfig.onExecute(event ->
            {
                var guitarName = event.getArgs()[1];
                var playerName = event.getArgs()[2];
                var guitar = guitarService.getByName(guitarName);
                if(guitar.isEmpty())
                {
                    FluentMessage.message()
                            .color(ChatColor.RED)
                            .text("Guitar")
                            .color(ChatColor.GRAY)
                            .text(guitarName)
                            .color(ChatColor.RED)
                            .text("not found")
                            .send(event.getSender());
                    return;
                }
                var player = Bukkit.getPlayer(playerName);
                if(player == null)
                {
                    FluentMessage.message()
                            .color(ChatColor.RED)
                            .text("player")
                            .color(ChatColor.GRAY)
                            .text(guitarName)
                            .color(ChatColor.RED)
                            .text("not found")
                            .send(event.getSender());
                    return;
                }
                player.getInventory().setItem(0,guitar.get().getCustomModel().getItemStack());
            });
        });
        return cmd;
    }

    public static CommandBuilder guitarVizualiztor() {

        var cmd = FluentCommand.create("vizualiztor");
        cmd.propertiesConfig(propertiesConfig ->
        {
            propertiesConfig.addPermissions(PluginPermissions.GIVE_CMD);
        });
        cmd.eventsConfig(eventConfig ->
        {
            eventConfig.onPlayerExecute(event ->
            {
                var go = new InstrumentPlayer();
                go.setPlayer(event.getPlayer());
                GameObjectManager.register(go,event.getPlayer().getLocation());
            });
        });
        return cmd;
    }

    private static void createGuitarArgument(ArgumentConfig config, InstrumentService guitarService)
    {
        config.addArgument("guitar-type",argumentBuilder ->
        {
            argumentBuilder.setType(ArgumentType.CUSTOM);
            argumentBuilder.setTabComplete(guitarService.getNames());
            argumentBuilder.setArgumentDisplay(ArgumentDisplay.TAB_COMPLETE);
            argumentBuilder.setDescription("select guitar type");
        });
    }




}
