package jw.instruments.core.extentions;

import jw.fluent.plugin.api.FluentApiSpigotBuilder;
import jw.fluent.plugin.api.FluentApiExtension;
import jw.fluent.plugin.implementation.FluentApi;
import jw.fluent.plugin.implementation.FluentApiSpigot;
import jw.instruments.core.data.Consts;
import jw.instruments.core.services.InstrumentService;
import jw.instruments.core.data.PluginConfig;
import jw.instruments.core.instuments.Instrument;
import jw.fluent.plugin.implementation.modules.messages.FluentMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;

public class GuitarExtention implements FluentApiExtension {


    @Override
    public void onConfiguration(FluentApiSpigotBuilder builder) {

    }

    @Override
    public void onFluentApiEnable(FluentApiSpigot fluentAPI) throws Exception {
        var config =  FluentApi.container().findInjection(PluginConfig.class);
        var instrumentService =  FluentApi.container().findInjection(InstrumentService.class);
      //TODO custom skins  registerCustomSkins(config, instrumentService);
      //  registerRecipe(config, instrumentService);
    }

    @Override
    public void onFluentApiDisabled(FluentApiSpigot fluentAPI) {

    }

    public void registerCustomSkins(PluginConfig config, InstrumentService instrumentService) {

        var parents = FluentMessage.message().text("Available parents");
        for (var parent : instrumentService.getInstruments()) {
            if (parent.getNamespaceKey().getKey().contains("custom")) {
                continue;
            }
            parents.text(" - ").text(parent.getName());
        }

        /*for (var customSkin : config.getCustomSkins()) {

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
        }*/
    }


    public void registerRecipe(PluginConfig pluginConfig, InstrumentService guitarService) {

        /*if (!pluginConfig.isCrating()) {
            return;
        }*/

        for (var guitar : guitarService.getInstruments()) {
            var recipe = guitar.getRecipe();
            Bukkit.addRecipe(recipe);
        }

        FluentApi.events().onEvent(CraftItemEvent.class, event ->
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
          /*  var hasPermission = PermissionsUtility.hasOnePermission(player,
                    PluginPermissions.CRAFTING);
            if (!hasPermission) {
                event.setCancelled(true);
            }*/
        });
    }



}
