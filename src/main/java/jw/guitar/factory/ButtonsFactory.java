package jw.guitar.factory;

import jw.guitar.chords.Chord;
import jw.spigot_fluent_api.fluent_gui.InventoryUI;
import jw.spigot_fluent_api.fluent_gui.button.ButtonUI;
import jw.spigot_fluent_api.fluent_gui.button.ButtonUIBuilder;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class ButtonsFactory
{
    public static List<ButtonUIBuilder> createChordsButtons()
    {
        var result = new ArrayList<ButtonUIBuilder>();
        for(var i =0;i< InventoryUI.INVENTORY_WIDTH;i++)
        {
            var btn = ButtonUI.builder()
                    .setMaterial(Material.GRAY_STAINED_GLASS_PANE)
                    .setTitlePrimary("Chord placeholder")
                    .setDescription(
                            "Click to change",
                            "Shift click to remove")
                    .setLocation(1, i)
                    .setActive(true);
            result.add(btn);
        }
        return result;
    }

}
