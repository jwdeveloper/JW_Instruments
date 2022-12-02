package jw.instruments.core.factory;

import jw.fluent_api.spigot.inventory_gui.InventoryUI;
import jw.fluent_api.spigot.inventory_gui.button.ButtonUI;
import jw.fluent_api.spigot.inventory_gui.button.ButtonUIBuilder;
import jw.fluent_api.spigot.messages.FluentMessage;
import jw.fluent_plugin.implementation.modules.translator.FluentTranslator;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class ButtonsFactory
{
    public static List<ButtonUIBuilder> createChordsButtons(FluentTranslator lang)
    {
        var description = FluentMessage
                .message()
                .field(lang.get("gui.base.left-click"),lang.get("gui.instrument.chord-placeholder.desc.left-click")).newLine()
                .field(lang.get("gui.base.shift-click"),lang.get("gui.instrument.chord-placeholder.desc.shift-click")).toArray();
        var result = new ArrayList<ButtonUIBuilder>();
        for(var i =0;i< InventoryUI.INVENTORY_WIDTH;i++)
        {
            var btn = ButtonUI.builder()
                    .setMaterial(Material.GRAY_STAINED_GLASS_PANE)
                    .setTitlePrimary(lang.get("gui.instrument.chord-placeholder.title"))
                    .setDescription()
                    .setDescription(description)
                    .setLocation(1, i)
                    .setActive(true);
            result.add(btn);
        }
        return result;
    }

}
