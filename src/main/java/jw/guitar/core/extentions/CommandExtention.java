package jw.guitar.core.extentions;

import jw.fluent_plugin.api.FluentApiBuilder;
import jw.fluent_plugin.api.FluentApiExtention;
import jw.fluent_plugin.implementation.FluentApi;
import jw.fluent_plugin.implementation.modules.logger.FluentLogger;
import jw.guitar.core.factory.CommandFactory;
import jw.guitar.core.managers.InstrumentManager;
import jw.guitar.core.services.InstrumentService;

public class CommandExtention implements FluentApiExtention {


    @Override
    public void onConfiguration(FluentApiBuilder fluentApiBuilder) {
        var builder = fluentApiBuilder.command();
        builder.eventsConfig(eventConfig ->
        {
            FluentLogger.LOGGER.log("Siema2");
            var manager = FluentApi.injection().findInjection(InstrumentManager.class);
            eventConfig.onPlayerExecute(event ->
            {
                FluentLogger.LOGGER.log("Siema");
                CommandFactory.guitarUiCmd(manager,event);
            });
        });
        builder.subCommandsConfig(subCommandConfig ->
        {
            var instrumentService =  FluentApi.injection().findInjection(InstrumentService.class);
            subCommandConfig.addSubCommand(CommandFactory.getInstrumentCmd(instrumentService));
            subCommandConfig.addSubCommand(CommandFactory.songsCmd());
            subCommandConfig.addSubCommand(CommandFactory.craftingCmd());
        });
    }

    @Override
    public void onFluentApiEnable(FluentApi fluentAPI) throws Exception {

    }

    @Override
    public void onFluentApiDisabled(FluentApi fluentAPI) {

    }
}
