package jw.instruments;

import jw.fluent.plugin.api.FluentApiBuilder;
import jw.fluent.plugin.implementation.FluentApi;
import jw.fluent.plugin.implementation.FluentPlugin;
import jw.instruments.core.data.Consts;
import jw.instruments.core.data.PluginPermissions;
import jw.instruments.core.rhythms.Rhythm;
import jw.instruments.core.instuments.Instrument;
import jw.instruments.core.extentions.ChordExtention;
import jw.instruments.core.extentions.CommandExtention;
import jw.instruments.core.extentions.GuitarExtention;
import jw.instruments.spigot.PluginDocumentation;


public final class Main extends FluentPlugin {

    @Override
    public void onConfiguration(FluentApiBuilder builder) {
        var container = builder.container();
        container.registerSingletonList(Instrument.class);
        container.registerSingletonList(Rhythm.class);
        container.addMetrics(Consts.BSTATS_ID);
        container.addResourcePack(options ->
        {
            options.setResourcepackUrl(Consts.RESOURCEPACK_URL);
            options.setLoadOnJoin(true);
        });
        container.addPlayerContext();
        container.addUpdater(updaterOptions ->
        {
              updaterOptions.setGithub(Consts.UPDATE_URL);
        });
        builder.command().setName(Consts.PLUGIN_NAMESPACE);

        builder.useExtention(new ChordExtention());
        builder.useExtention(new GuitarExtention());
        builder.useExtention(new CommandExtention());

        container.addDocumentation(options ->
        {
            options.setPermissionModel(PluginPermissions.class);
            options.addDecorator(new PluginDocumentation());
        });
        builder.permissions().setBasePermissionName(PluginPermissions.PLUGIN);
    }

    @Override
    public void onFluentApiEnable(FluentApi fluentAPI) {


    }

    @Override
    public void onFluentApiDisabled(FluentApi fluentAPI) {

    }
}
