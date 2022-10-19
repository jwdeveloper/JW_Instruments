package jw.guitar.plugin_setup;

import jw.guitar.data.Consts;
import jw.guitar.data.PluginConfig;
import jw.guitar.data.PluginPermissions;
import jw.guitar.gameobjects.instuments.Instrument;
import jw.guitar.services.InstrumentService;
import jw.fluent_api.desing_patterns.dependecy_injection.FluentInjection;
import jw.fluent_api.minecraft.events.FluentEvent;
import jw.fluent_api.minecraft.logger.FluentLogger;
import jw.fluent_api.minecraft.messages.FluentMessage;
import jw.fluent_plugin.FluentPlugin;
import jw.fluent_plugin.starup_actions.api.PluginPipeline;
import jw.fluent_plugin.starup_actions.data.PipelineOptions;
import jw.fluent_api.utilites.PermissionsUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;

public class GuitarSetup implements PluginPipeline {

    @Override
    public void pluginEnable(PipelineOptions options) throws Exception {
        var config = FluentInjection.findInjection(PluginConfig.class);
        var instrumentService = FluentInjection.findInjection(InstrumentService.class);
        registerCustomSkins(config, instrumentService);
        registerRecipe(config, instrumentService);

    }

    public void registerCustomSkins(PluginConfig config, InstrumentService instrumentService) {

        var parents = FluentMessage.message().text("Available parents");
        for (var parent : instrumentService.getInstruments()) {
            if (parent.getNamespaceKey().getKey().contains("custom")) {
                continue;
            }
            parents.text(" - ").text(parent.getName());
        }

        for (var customSkin : config.getCustomSkins()) {

            if(customSkin.getCraftingMaterial() == null)
            {
                FluentLogger.warning("Unable to load skin " + customSkin.getName(),
                        "Invalid crafting material " + customSkin.getCraftingMaterial());
                continue;
            }

            var result = instrumentService.addCustomInstruments(customSkin);
            if (result) {
                continue;
            }

            FluentLogger.warning("Unable to load skin " + customSkin.getName(),
                    "Parent " + customSkin.getParent() + " not found");
            FluentLogger.info(parents.toString());
        }
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
            if (!(event.getWhoClicked() instanceof Player player)) {
                return;
            }
            var recipe = event.getRecipe();
            if (recipe.getResult().getType() != Consts.MODEL_MATERIAL) {
                return;
            }
            if (!Instrument.isInstrument(recipe.getResult())) {
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
