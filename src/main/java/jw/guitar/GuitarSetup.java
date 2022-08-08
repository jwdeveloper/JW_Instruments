package jw.guitar;

import jw.guitar.data.Consts;
import jw.guitar.data.PluginConfig;
import jw.guitar.data.PluginPermissions;
import jw.guitar.factory.CommandFactory;
import jw.guitar.gameobjects.instuments.Instrument;
import jw.guitar.rhythms.Rhythm;
import jw.guitar.services.InstrumentService;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.FluentInjection;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.enums.LifeTime;
import jw.spigot_fluent_api.fluent_commands.api.builder.CommandBuilder;
import jw.spigot_fluent_api.fluent_events.FluentEvent;
import jw.spigot_fluent_api.fluent_logger.FluentLogger;
import jw.spigot_fluent_api.fluent_plugin.FluentPlugin;
import jw.spigot_fluent_api.fluent_plugin.starup_actions.pipeline.PluginPipeline;
import jw.spigot_fluent_api.fluent_plugin.starup_actions.pipeline.data.PipelineOptions;
import jw.spigot_fluent_api.utilites.PermissionsUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;

public class GuitarSetup implements PluginPipeline {

    @Override
    public void pluginEnable(PipelineOptions options) throws Exception {


      //  injectInstruments(options.getPlugin());
       // injectPlayingStyles(options.getPlugin());

        var config = FluentInjection.getInjection(PluginConfig.class);
        var guitarService = FluentInjection.getInjection(InstrumentService.class);
        registerRecipe(config, guitarService);
        registerCommands(options.getDefaultCommand().getBuilder(), guitarService);
    }

    public void injectInstruments(FluentPlugin plugin)
    {
        var instruments = plugin.getTypeManager().getByInterface(Instrument.class);
        FluentLogger.log("siema22",instruments.size());
        for(var instrument : instruments)
        {

            FluentInjection.getInjectionContainer().register(instrument, LifeTime.TRANSIENT);
        }
    }

    public void injectPlayingStyles(FluentPlugin plugin)
    {
        var styles = plugin.getTypeManager().getByInterface(Rhythm.class);
        for(var style : styles)
        {
            FluentInjection.getInjectionContainer().register(style, LifeTime.TRANSIENT);
        }
    }

    public void registerCommands(CommandBuilder builder, InstrumentService guitarService)
    {
        builder.subCommandsConfig(subCommandConfig ->
        {
            subCommandConfig.addSubCommand(CommandFactory.getCmd(guitarService));
            subCommandConfig.addSubCommand(CommandFactory.giveCmd(guitarService));
            subCommandConfig.addSubCommand(CommandFactory.songsCmd());
            subCommandConfig.addSubCommand(CommandFactory.guitarVizualiztor());
        });
    }

    public void registerRecipe(PluginConfig pluginConfig, InstrumentService guitarService) {

        if (!pluginConfig.isCrating()) {
            return;
        }

        for (var guitar : guitarService.getInstruments()) {
            var recipe = guitar.getRecipe();
            Bukkit.addRecipe(recipe);
        }

        FluentEvent.onEvent(CraftItemEvent.class, event ->
        {
            if (!(event.getWhoClicked() instanceof Player)) {
                return;
            }
            var player = (Player) event.getWhoClicked();
            var recipe = event.getRecipe();
            if (recipe.getResult().getType() != Consts.MODEL_MATERIAL)
            {
                return;
            }
            if (!Instrument.isInstrument(recipe.getResult()))
            {
                return;
            }
            var hasPermission = PermissionsUtility.hasOnePermission(player,
                    PluginPermissions.CRAFTING);
            if (!hasPermission) {
                event.setCancelled(true);
            }
        });
    }

    @Override
    public void pluginDisable(FluentPlugin fluentPlugin) throws Exception {

    }
}
