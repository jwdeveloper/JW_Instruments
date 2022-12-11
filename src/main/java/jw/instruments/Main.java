package jw.instruments;

import jw.fluent.plugin.api.FluentApiSpigotBuilder;
import jw.fluent.plugin.implementation.FluentApi;
import jw.fluent.plugin.implementation.FluentApiSpigot;
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
    public void onConfiguration(FluentApiSpigotBuilder builder) {
        var container = builder.container();
        container.registerSingletonList(Instrument.class);
        container.registerSingletonList(Rhythm.class);
        container.addMetrics(Consts.BSTATS_ID);
        container.addResourcePack(options ->
        {
            options.setDefaultUrl(Consts.RESOURCEPACK_URL);
            options.setLoadOnJoin(true);
        });
        container.addPlayerContext();
        container.addUpdater(updaterOptions ->
        {
              updaterOptions.setGithub(Consts.GITHUB_URL);
        });
        builder.defaultCommand().setName(Consts.PLUGIN_NAMESPACE);

        builder.useExtension(new ChordExtention());
        builder.useExtension(new GuitarExtention());
        builder.useExtension(new CommandExtention());

        container.addDocumentation(options ->
        {
            options.setPermissionModel(PluginPermissions.class);
            options.addSection(new PluginDocumentation());

            if(FluentApi.plugin().getDescription().getVersion().equals("${version}"))
            {
                options.setUseGithubDocumentation(true);
                options.setUseSpigotDocumentation(true);
            }

        });
        builder.permissions().setBasePermissionName(PluginPermissions.PLUGIN);
    }

    @Override
    public void onFluentApiEnable(FluentApiSpigot fluentAPI) {




    }

    @Override
    public void onFluentApiDisabled(FluentApiSpigot fluentAPI) {

    }
}
