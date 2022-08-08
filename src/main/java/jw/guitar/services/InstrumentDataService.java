package jw.guitar.services;

import jw.guitar.data.instument.InstrumentData;
import jw.guitar.gameobjects.instuments.Instrument;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.annotations.Inject;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.annotations.Injection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

@Injection
public class InstrumentDataService
{
    private final InstrumentService service;

    @Inject
    public InstrumentDataService(InstrumentService service)
    {
        this.service = service;
    }

    public Optional<InstrumentData> get(ItemStack itemStack)
    {
        if(!Instrument.isInstrument(itemStack))
        {
            return Optional.empty();
        }
        var name =  itemStack.getItemMeta()
                .getPersistentDataContainer()
                .get(Instrument.NAMESPACE, PersistentDataType.STRING);

        var exists = service.getByName(name);
        if(exists.isEmpty())
        {
            return Optional.empty();
        }

        var result = new InstrumentData();
        result.setCustomModel(exists.get().getCustomModel());
        result.setSound(name);
        return Optional.of(result);
    }
}
