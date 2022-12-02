package jw.instruments;

import jw.fluent_api.spigot.permissions.implementation.PermissionsUtility;
import jw.fluent_plugin.api.FluentApiBuilder;
import jw.fluent_plugin.implementation.FluentApi;
import jw.fluent_plugin.implementation.FluentPlugin;
import jw.instruments.core.data.Consts;
import jw.instruments.core.data.PluginPermissions;
import jw.instruments.core.rhythms.Rhythm;
import jw.instruments.core.instuments.Instrument;
import jw.instruments.core.extentions.ChordExtention;
import jw.instruments.core.extentions.CommandExtention;
import jw.instruments.core.extentions.GuitarExtention;
import org.bukkit.Bukkit;

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
              updaterOptions.setGithub("https://github.com/jwdeveloper/JW_Piano/releases/download/1.1.3/JW_Piano.jar");
        });
        builder.command().setName(Consts.PLUGIN_NAMESPACE);

        builder.useExtention(new ChordExtention());
        builder.useExtention(new GuitarExtention());
        builder.useExtention(new CommandExtention());

        container.addDocumentation(updaterOptions ->
        {
            updaterOptions.setPermissionModel(PluginPermissions.class);
        });
        builder.permissions().setBasePermission(PluginPermissions.PLUGIN);
    }

    @Override
    public void onFluentApiEnable(FluentApi fluentAPI) {
        fluentAPI.getFluentLogger().info("Server permissions", fluentAPI.getFluentPermission().getPermissions().size());
        for(var p : Bukkit.getOnlinePlayers())
        {
            PermissionsUtility.showPlayerPermissions(p);
        }
    }

    @Override
    public void onFluentApiDisabled(FluentApi fluentAPI) {

    }
}
