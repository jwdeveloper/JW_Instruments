package jw.guitar;

import jw.fluent_api.desing_patterns.dependecy_injection.api.enums.LifeTime;
import jw.fluent_plugin.api.FluentApiBuilder;
import jw.fluent_plugin.implementation.FluentApi;
import jw.fluent_plugin.implementation.FluentPlugin;
import jw.guitar.core.data.Consts;
import jw.guitar.core.rhythms.Rhythm;
import jw.guitar.spigot.gameobjects.instuments.Instrument;
import jw.guitar.core.extentions.ChordExtention;
import jw.guitar.core.extentions.CommandExtention;
import jw.guitar.core.extentions.GuitarExtention;

public final class Main extends FluentPlugin {


    @Override
    public void onConfiguration(FluentApiBuilder builder) {

     //   builder.useDefaultNamespace(Consts.NAMESPACE);

        var container = builder.container();
        container.registerSingletonList(Instrument.class);
        container.registerSingletonList(Rhythm.class);
        container.addMetrics(Consts.BSTATS_ID);
        container.addResourcePacks(options ->
        {
            options.setResourcepackUrl(Consts.RESOURCEPACK_URL);
            options.setLoadOnJoin(true);
        });
        container.addPlayerContext(playerContainerBuilder ->
        {

        });

        builder.useExtention(new ChordExtention());
        builder.useExtention(new GuitarExtention());
        builder.useExtention(new CommandExtention());
    }

    @Override
    public void onFluentApiEnable(FluentApi fluentAPI) {

    }

    @Override
    public void onFluentApiDisabled(FluentApi fluentAPI) {

    }
}
