package jw.guitar.plugin_setup;

import jw.fluent_plugin.implementation.FluentAPI;
import jw.guitar.factory.CommandFactory;
import jw.guitar.managers.InstrumentManager;
import jw.guitar.services.InstrumentService;
import jw.fluent_api.spigot.commands.api.builder.CommandBuilder;
import jw.fluent_plugin.implementation.FluentPlugin;
import jw.fluent_plugin.api.PluginAction;
import jw.fluent_plugin.api.options.PipelineOptions;

public class CommandSetup implements PluginAction {
    @Override
    public void pluginEnable(PipelineOptions options) throws Exception {
        var instrumentService =  FluentAPI.injection().findInjection(InstrumentService.class);
        registerCommands(options.getDefaultCommand().getBuilder(), instrumentService);
    }

    public void registerCommands(CommandBuilder builder, InstrumentService guitarService) {
        var manager =  FluentAPI.injection().findInjection(InstrumentManager.class);
        builder.eventsConfig(eventConfig ->
        {
            eventConfig.onPlayerExecute(event ->
            {
                CommandFactory.guitarUiCmd(manager,event);
            });
        });
        builder.subCommandsConfig(subCommandConfig ->
        {
            subCommandConfig.addSubCommand(CommandFactory.getCmd(guitarService));
            subCommandConfig.addSubCommand(CommandFactory.songsCmd());
            subCommandConfig.addSubCommand(CommandFactory.craftingCmd());
        });
    }

    @Override
    public void pluginDisable(FluentPlugin fluentPlugin) throws Exception {

    }
}
