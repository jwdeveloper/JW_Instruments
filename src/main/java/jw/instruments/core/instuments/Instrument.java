package jw.instruments.core.instuments;

import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent.api.spigot.gameobjects.api.models.CustomModel;
import jw.fluent.plugin.implementation.FluentApi;
import jw.fluent.plugin.implementation.FluentApiSpigot;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.persistence.PersistentDataType;

@Injection
public interface Instrument {

    ShapedRecipe getRecipe();

    CustomModel getCustomModel();

    default NamespacedKey getNamespaceKey() {
        return new NamespacedKey(FluentApi.plugin(), getName() + "_instrument");
    }

    default String getName() {
        return getClass().getSimpleName().toLowerCase();
    }


    static NamespacedKey NAMESPACE = new NamespacedKey(FluentApi.plugin(), "instrument");


    static boolean isInstrument(ItemStack itemStack) {
        if(itemStack == null)
            return false;
        if(itemStack.getItemMeta() == null)
            return false;

        return itemStack.getItemMeta().getPersistentDataContainer().has(NAMESPACE, PersistentDataType.STRING);

    }
}
