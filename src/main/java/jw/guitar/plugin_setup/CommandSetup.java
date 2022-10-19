package jw.guitar.plugin_setup;

import jw.guitar.factory.CommandFactory;
import jw.guitar.managers.InstrumentManager;
import jw.guitar.services.InstrumentService;
import jw.fluent_api.desing_patterns.dependecy_injection.FluentInjection;
import jw.fluent_api.minecraft.commands.api.builder.CommandBuilder;
import jw.fluent_plugin.FluentPlugin;
import jw.fluent_plugin.starup_actions.api.PluginPipeline;
import jw.fluent_plugin.starup_actions.data.PipelineOptions;

public class CommandSetup implements PluginPipeline {
    @Override
    public void pluginEnable(PipelineOptions options) throws Exception {
        var instrumentService = FluentInjection.findInjection(InstrumentService.class);
        registerCommands(options.getDefaultCommand().getBuilder(), instrumentService);
    }

    public void registerCommands(CommandBuilder builder, InstrumentService guitarService) {
        var manager = FluentInjection.findInjection(InstrumentManager.class);
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
