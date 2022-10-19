package jw.guitar.gameobjects.instuments;

import jw.spigot_fluent_api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.spigot_fluent_api.fluent_gameobjects.api.models.CustomModel;
import jw.spigot_fluent_api.fluent_plugin.FluentPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.persistence.PersistentDataType;

@Injection
public interface Instrument {

    ShapedRecipe getRecipe();

    CustomModel getCustomModel();

    default NamespacedKey getNamespaceKey() {
        return new NamespacedKey(FluentPlugin.getPlugin(), getName() + "_guitar");
    }

    default String getName() {
        return getClass().getSimpleName().toLowerCase();
    }


    static NamespacedKey NAMESPACE = new NamespacedKey(FluentPlugin.getPlugin(), "instrument");


    static boolean isInstrument(ItemStack itemStack) {


        if(itemStack == null)
            return false;
        if(itemStack.getItemMeta() == null)
            return false;

        return itemStack.getItemMeta().getPersistentDataContainer().has(NAMESPACE, PersistentDataType.STRING);

    }
}
