package jw.guitar.chords;

import jw.spigot_fluent_api.fluent_logger.FluentLogger;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;


public record Chord(String key, String suffix, List<Note> notes) {

    public String fullName() {
        return key + " " + suffix;
    }

    public ItemStack getItemStack() {
        var itemstack = new ItemStack(Material.STRING);
        var meta = itemstack.getItemMeta();
        meta.setDisplayName(fullName());
        meta.setCustomModelData(getCustomId());
        itemstack.setItemMeta(meta);
        return itemstack;
    }


    public int getCustomId()
    {
        FluentLogger.log(key+" ");
        var key_ = key.toLowerCase();
        switch (key_)
        {
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
