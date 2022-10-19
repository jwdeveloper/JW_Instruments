package jw.guitar.factory;

import jw.fluent_api.minecraft.gui.InventoryUI;
import jw.fluent_api.minecraft.gui.button.ButtonUI;
import jw.fluent_api.minecraft.gui.button.ButtonUIBuilder;
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
