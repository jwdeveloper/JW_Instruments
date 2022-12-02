package jw.instruments.core.data.instument;

import jw.fluent_api.desing_patterns.observer.implementation.ObserverBag;
import jw.fluent_api.spigot.inventory_gui.InventoryUI;
import jw.fluent_plugin.implementation.FluentApi;
import jw.instruments.core.rhythms.Rhythm;
import lombok.Data;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;


@Data
public class InstrumentDataObserver {
    private ObserverBag<String> instumentName;
    private ObserverBag<Boolean> displayChordsName;
    private ObserverBag<Boolean> changeRhytmOnShift;
    private ObserverBag<String[]> chords;
    private ObserverBag<Integer> volume;

    private ObserverBag<Rhythm> rhythm;
    private ItemStack itemStack;

    private final int CHORD_SIZE = 9;
    public InstrumentDataObserver(ItemStack itemStack, String instumentName)
    {
        this.itemStack = itemStack;
        chords = new ObserverBag<>(new String[CHORD_SIZE]);
        this.instumentName = new ObserverBag<>(instumentName);
        displayChordsName = new ObserverBag<>(true);
        changeRhytmOnShift = new ObserverBag<>(true);
        volume  = new ObserverBag<>(100);
    }

    public void setChord(int index, String name) {
        var list = chords.get();
        list[index] = name;
        refreshChords();
    }

    public void updateChords(Map<Integer,String> input)
    {
        var chordsList = chords.get();
        for(var i =0;i<CHORD_SIZE;i++)
        {
            chordsList[i] = input.get(i);
        }
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
        var nameSpace = new NamespacedKey(FluentApi.plugin(), "chords");
        container.set(nameSpace, PersistentDataType.STRING, result);
        chords.getObserver().set(chords2);
    }

}