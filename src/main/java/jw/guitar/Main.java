package jw.guitar;

import jw.guitar.data.Consts;
import jw.guitar.plugin_setup.ChordSetup;
import jw.guitar.plugin_setup.CommandSetup;
import jw.guitar.plugin_setup.GuitarSetup;
import jw.fluent_plugin.FluentPlugin;
import jw.fluent_plugin.config.ConfigFile;
import jw.fluent_plugin.starup_actions.api.PluginConfiguration;

public final class Main extends FluentPlugin {

    @Override
    protected void OnConfiguration(PluginConfiguration configuration, ConfigFile configFile) {

        configuration
                .configurePlugin(pluginOptions ->
                {
                    pluginOptions.useMetrics(Consts.BSTATS_ID);
                    pluginOptions.useDefaultNamespace(Consts.NAMESPACE);
                    pluginOptions.useResourcePack(Consts.RESOURCEPACK_URL);
                   // pluginOptions.useUpdate(Consts.UPDATE_URL);
                })
                .useDebugMode()
                .useFilesHandler()
                .useCustomAction(new ChordSetup())
                .useCustomAction(new GuitarSetup())
                .useCustomAction(new CommandSetup());

    }

    @Override
    protected void OnFluentPluginEnable() {

    }

    @Override
    protected void OnFluentPluginDisable() {

    }
}
