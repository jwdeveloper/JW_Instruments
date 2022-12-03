package jw.instruments.core.data.instrument;

import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent_api.desing_patterns.dependecy_injection.api.enums.LifeTime;
import jw.fluent_api.desing_patterns.observer.implementation.Observer;
import jw.fluent_api.desing_patterns.observer.implementation.ObserverBag;
import jw.fluent_api.spigot.inventory_gui.InventoryUI;
import jw.fluent_plugin.implementation.FluentApi;
import jw.fluent_plugin.implementation.modules.logger.FluentLogger;
import jw.instruments.core.rhythms.Rhythm;
import lombok.Data;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.beans.Transient;
import java.util.Map;


@Injection(lifeTime = LifeTime.TRANSIENT)
public class InstrumentItemStackObserver {
    @Getter
    private Observer<Boolean> displayChordsName;
    @Getter
    private Observer<Boolean> changeRhytmOnShift;
    @Getter
    private Observer<String[]> chords;
    @Getter
    private Observer<Integer> volume;

    private InstrumentItemStack instrumentItemStack;

    public InstrumentItemStackObserver() {
        var placeholder = new InstrumentData();
        chords = new Observer(placeholder, "chords");
        volume = new Observer(placeholder, "volume");
        displayChordsName = new ObserverBag(true).getObserver();
        changeRhytmOnShift = new ObserverBag(true).getObserver();
    }

    public void setInstrument(InstrumentItemStack itemStack)
    {
        this.instrumentItemStack = itemStack;
        chords.setObject(itemStack.getInstrumentData());
        volume.setObject(itemStack.getInstrumentData());
    }

    public String getInstrumentName() {
        return instrumentItemStack.getInstrumentData().getName();
    }

    public void setChord(int index, String name) {
        var list = chords.get();
        list[index] = name;
        refreshChords();
    }

    public void updateChords(Map<Integer, String> input) {
        var chordsList = chords.get();
        for (var i = 0; i < InventoryUI.INVENTORY_WIDTH; i++) {
            chordsList[i] = input.get(i);
        }
        refreshChords();
    }

    private void refreshChords() {
        chords.set(chords.get());
    }

}