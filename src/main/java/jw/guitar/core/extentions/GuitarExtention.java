package jw.guitar.core.extentions;

import jw.fluent_plugin.api.FluentApiBuilder;
import jw.fluent_plugin.api.FluentApiExtention;
import jw.fluent_plugin.implementation.FluentApi;
import jw.guitar.core.data.Consts;
import jw.guitar.core.data.PluginPermissions;
import jw.guitar.core.services.InstrumentService;
import jw.guitar.core.data.PluginConfig;
import jw.guitar.spigot.gameobjects.instuments.Instrument;
import jw.fluent_api.spigot.events.FluentEvent;
import jw.fluent_api.spigot.messages.FluentMessage;
import jw.fluent_api.utilites.PermissionsUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;

public class GuitarExtention implements FluentApiExtention {


    @Override
    public void onConfiguration(FluentApiBuilder builder) {

    }

    @Override
    public void onFluentApiEnable(FluentApi fluentAPI) throws Exception {
        var config =  FluentApi.injection().findInjection(PluginConfig.class);
        var instrumentService =  FluentApi.injection().findInjection(InstrumentService.class);
        registerCustomSkins(config, instrumentService);
        registerRecipe(config, instrumentService);
    }

    @Override
    public void onFluentApiDisabled(FluentApi fluentAPI) {

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
                FluentApi.logger().warning("Unable to load skin " + customSkin.getName(),
                        "Invalid crafting material " + customSkin.getCraftingMaterial());
                continue;
            }

            var result = instrumentService.addCustomInstruments(customSkin);
            if (result) {
                continue;
            }

            FluentApi.logger().warning("Unable to load skin " + customSkin.getName(),
                    "Parent " + customSkin.getParent() + " not found");
            FluentApi.logger().info(parents.toString());
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



}
