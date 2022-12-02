package jw.instruments.core.extentions;

import jw.fluent_plugin.api.FluentApiBuilder;
import jw.fluent_plugin.api.FluentApiExtention;
import jw.fluent_plugin.implementation.FluentApi;
import jw.instruments.core.data.PluginPermissions;
import jw.instruments.core.factory.CommandFactory;
import jw.instruments.core.managers.InstrumentManager;
import jw.instruments.core.services.InstrumentService;

public class CommandExtention implements FluentApiExtention {


    @Override
    public void onConfiguration(FluentApiBuilder fluentApiBuilder) {
        var builder = fluentApiBuilder.command();
        fluentApiBuilder.container().configure(configuration ->
        {
           configuration.onRegistration(onRegistrationEvent ->
           {
               return true;
           });
        });
        builder.propertiesConfig(propertiesConfig ->
        {
           propertiesConfig.addPermissions(PluginPermissions.INSTRUMENT_CMD);
        });
        builder.eventsConfig(eventConfig ->
        {
            var manager = FluentApi.injection().findInjection(InstrumentManager.class);
            eventConfig.onPlayerExecute(event ->
            {
                CommandFactory.instumentUI(manager,event);
            });
        });
        builder.subCommandsConfig(subCommandConfig ->
        {
            var instrumentService =  FluentApi.injection().findInjection(InstrumentService.class);
            subCommandConfig.addSubCommand(CommandFactory.songsListCmd());
            subCommandConfig.addSubCommand(CommandFactory.getInstrumentCmd(instrumentService));
        });
    }

    @Override
    public void onFluentApiEnable(FluentApi fluentAPI) throws Exception {

    }

    @Override
    public void onFluentApiDisabled(FluentApi fluentAPI) {

    }
}
