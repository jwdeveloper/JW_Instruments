package jw.instruments.core.extentions;

import jw.fluent.plugin.implementation.modules.messages.FluentMessage;
import jw.fluent.plugin.api.FluentApiSpigotBuilder;
import jw.fluent.plugin.api.FluentApiExtension;
import jw.fluent.plugin.implementation.FluentApi;
import jw.fluent.plugin.implementation.FluentApiSpigot;
import jw.instruments.core.data.PluginPermissions;
import jw.instruments.core.services.CommandsService;
import jw.instruments.core.managers.InstrumentManager;

public class CommandExtention implements FluentApiExtension {


    @Override
    public void onConfiguration(FluentApiSpigotBuilder fluentApiBuilder) {
        var defaultCommand = fluentApiBuilder.defaultCommand();
        defaultCommand.propertiesConfig(propertiesConfig ->
        {
            propertiesConfig.addPermissions(PluginPermissions.INSTRUMENT_CMD);
            propertiesConfig.setUsageMessage("/instrument or /instrument <children>");
            propertiesConfig.setDescription("opens instrument configuration GUI where player can modify behaviour currently using");
        });
        defaultCommand.eventsConfig(eventConfig ->
        {
            var manager = FluentApi.container().findInjection(InstrumentManager.class);
            eventConfig.onPlayerExecute(event ->
            {
                final var player = event.getPlayer();
                if (!manager.validatePlayer(player)) {
                    FluentMessage.message()
                            .info()
                            .textSecondary("You need to hold instrument in your left hand.")
                            .textSecondary(" Use ").textPrimary("/instrument get <name>")
                            .textSecondary(" to obtain instrument and then press")
                            .textPrimary(" 'F' ").textSecondary(" key")
                            .send(player);
                    return;
                }
                manager.get(player).openGUI();
            });
        });
        defaultCommand.subCommandsConfig(subCommandConfig ->
        {
            var instrumentService = FluentApi.container().findInjection(CommandsService.class);
            subCommandConfig.addSubCommand(instrumentService.createSongsListCmd());
            subCommandConfig.addSubCommand(instrumentService.createGetInstrumentCmd());
        });
    }

    @Override
    public void onFluentApiEnable(FluentApiSpigot fluentAPI) throws Exception {

    }

    @Override
    public void onFluentApiDisabled(FluentApiSpigot fluentAPI) {

    }
}
