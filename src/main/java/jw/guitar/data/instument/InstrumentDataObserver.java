package jw.guitar.data.instument;

import jw.fluent_api.desing_patterns.observer.implementation.ObserverBag;
import jw.fluent_api.minecraft.gui.InventoryUI;
import jw.fluent_plugin.FluentPlugin;
import lombok.Data;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;


@Data
public class InstrumentDataObserver {
    private ObserverBag<String> instumentName;
    private ObserverBag<Boolean> displayNames;
    private ObserverBag<String[]> chords;

    private ObserverBag<Integer> volume;
    private ItemStack itemStack;
    public InstrumentDataObserver(ItemStack itemStack, String instumentName)
    {
        this.itemStack = itemStack;
        chords = new ObserverBag<>(new String[8]);
        this.instumentName = new ObserverBag<>(instumentName);
        displayNames = new ObserverBag<>(true);
    }

    public void setChords(int index, String name) {
        var chords2 = chords.get();
        chords2[index] = name;
        refreshChords();
    }

    private void refreshChords() {
        var chords2 = chords.get();
        var builder = new StringBuilder();
        for(var i =0;i< InventoryUI.INVENTORY_WIDTH;i++)
        {
            var value = chords.get()[i];
            if(value == null)
            {
                value = "EMPTY";
            }
            builder.append(value)
                    .append(";");

        }
        var result = builder.toString();
        var container = itemStack.getItemMeta().getPersistentDataContainer();
        var nameSpace = new NamespacedKey(FluentPlugin.getPlugin(), "chords");
        container.set(nameSpace, PersistentDataType.STRING, result);
        chords.getObserver().set(chords2);
    }

}