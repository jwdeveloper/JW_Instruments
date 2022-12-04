package jw.instruments.core.services;

import jw.instruments.core.data.instument.InstrumentDataObserver;
import jw.instruments.core.instuments.Instrument;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Inject;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Injection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

@Injection
public class InstrumentDataService
{
    private final InstrumentService instrumentService;

    @Inject
    public InstrumentDataService(InstrumentService service)
    {
        this.instrumentService = service;
    }

    public Optional<InstrumentDataObserver> get(ItemStack itemStack)
    {
        if(!Instrument.isInstrument(itemStack))
        {
            return Optional.empty();
        }
        var name =  itemStack.getItemMeta()
                .getPersistentDataContainer()
                .get(Instrument.NAMESPACE, PersistentDataType.STRING);

        var exists = instrumentService.getByName(name);
        if(exists.isEmpty())
        {
            return Optional.empty();
        }

        var result = new InstrumentDataObserver(itemStack, name);
        return Optional.of(result);
    }
}