package jw.instruments.core.data.chords;

import jw.fluent_plugin.implementation.FluentApi;
import jw.instruments.core.data.Consts;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;


public record Chord(String key, String suffix, Integer fret, List<Note> notes) {

    public String fullName() {
        return key + " " + suffix;
    }

    public ItemStack getItemStack() {
        var itemStack = new ItemStack(Consts.MODEL_MATERIAL);
        var meta = itemStack.getItemMeta();
        meta.setDisplayName(fullName());
        meta.setCustomModelData(getCustomId());
        meta.getPersistentDataContainer().set(getNamespace(), PersistentDataType.STRING,fullName());
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private static NamespacedKey getNamespace() {
        return new NamespacedKey(FluentApi.plugin(), "chord");
    }

    public int getCustomId() {
        var key_ = key.toLowerCase();
        switch (key_) {
            case "c":
                return 300;
            case "c#":
                return 301;
            case "d":
                return 302;
            case "eb":
                return 303;
            case "e":
                return 304;
            case "f":
                return 305;
            case "f#":
                return 306;
            case "g":
                return 307;
            case "ab":
                return 308;
            case "a":
                return 309;
            case "bb":
                return 310;
            case "b":
                return 311;
        }
        return 300;
    }

}
