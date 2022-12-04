package jw.instruments.core.services;

import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Inject;
import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent.api.utilites.InventoryUtility;
import jw.fluent.plugin.implementation.FluentApi;
import jw.instruments.core.data.PluginPermissions;
import jw.instruments.spigot.gui.songs.SongsListGui;
import jw.fluent.api.spigot.commands.FluentCommand;
import jw.fluent.api.spigot.commands.api.builder.CommandBuilder;
import jw.fluent.api.spigot.commands.api.enums.ArgumentDisplay;
import jw.fluent.api.spigot.commands.api.enums.ArgumentType;
import jw.fluent.api.spigot.messages.FluentMessage;
import org.bukkit.ChatColor;


@Injection
public class CommandsService {

    private final InstrumentService instrumentService;
    private final InstrumentItemStackFactory factory;

    @Inject
    public CommandsService(InstrumentService instrumentService, InstrumentItemStackFactory factory)
    {
        this.instrumentService = instrumentService;
        this.factory =factory;
    }


    public CommandBuilder createSongsListCmd() {

        var cmd = FluentCommand.create("songs");
        cmd.propertiesConfig(propertiesConfig ->
        {
            propertiesConfig.addPermissions(PluginPermissions.SONGS_CMD);
            propertiesConfig.setUsageMessage("/instrument songs");
            propertiesConfig.setDescription("opens GUI where you can Edit, Create, Delete songs");
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

    public  CommandBuilder createGetInstrumentCmd() {
        var cmd = FluentCommand.create("get");
        cmd.propertiesConfig(propertiesConfig ->
        {
            propertiesConfig.addPermissions(PluginPermissions.GET_CMD);
            propertiesConfig.setUsageMessage("/instrument get <instrument-type>");
            propertiesConfig.setDescription("by trigger this player will get selected instrument");
        });
        cmd.argumentsConfig(argumentConfig ->
        {
            argumentConfig.addArgument("instrument-type", argumentBuilder ->
            {
                argumentBuilder.setType(ArgumentType.CUSTOM);
                argumentBuilder.setTabComplete(instrumentService.getNames());
                argumentBuilder.setArgumentDisplay(ArgumentDisplay.TAB_COMPLETE);
                argumentBuilder.setDescription("select instrument type");
            });
        });
        cmd.eventsConfig(eventConfig ->
        {
            eventConfig.onPlayerExecute(event ->
            {
                var name = event.getArgs()[1];
                var optional = instrumentService.getByName(name);
                if (optional.isEmpty()) {
                    FluentMessage.message()
                            .error()
                            .text("Instrument ")
                            .color(ChatColor.GRAY)
                            .text(name)
                            .color(ChatColor.RED)
                            .text(" not found ")
                            .send(event.getPlayer());
                    return;
                }
                var instrument = optional.get();
                var optionalItemstack = factory.create(instrument);
                if (optionalItemstack.isEmpty()) {
                    FluentMessage.message()
                            .error()
                            .text("Unable to create guitar ")
                            .send(event.getPlayer());
                    return;
                }
                var instrumentItemStack = optionalItemstack.get();
                InventoryUtility.giveToEmptySlotOrDrop(event.getPlayer(),instrumentItemStack.getItemStack(),true);
                FluentMessage.message()
                        .info()
                        .textSecondary("Press ")
                        .textPrimary('f')
                        .textSecondary(" to use instrument")
                        .send(event.getPlayer());
            });
        });
        return cmd;
    }
}
