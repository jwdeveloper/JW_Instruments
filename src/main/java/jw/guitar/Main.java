package jw.guitar;

import jw.guitar.data.PluginPermissions;
import jw.spigot_fluent_api.fluent_plugin.FluentPlugin;
import jw.spigot_fluent_api.fluent_plugin.config.ConfigFile;
import jw.spigot_fluent_api.fluent_plugin.starup_actions.PluginConfiguration;

public final class Main extends FluentPlugin {

    @Override
    protected void OnConfiguration(PluginConfiguration configuration, ConfigFile configFile) {

        configuration
                .useDebugMode()
                .useFilesHandler()
                .userDefaultPermission(PluginPermissions.BASE)
                .useDefaultCommand(PluginPermissions.BASE)
                .useCustomAction(new ChordSetup())
                .useCustomAction(new GuitarSetup());
    }

    @Override
    protected void OnFluentPluginEnable() {

    }

    @Override
    protected void OnFluentPluginDisable() {

    }
}
