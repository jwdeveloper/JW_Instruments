package jw.instruments.core.services;


import jw.instruments.core.data.CustomSkin;
import jw.instruments.core.instuments.CustomInstrument;
import jw.instruments.core.instuments.Instrument;
import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Inject;
import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Injection;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Injection
public class InstrumentService {
    @Getter
    private final List<Instrument> instruments;

    @Inject
    public InstrumentService(List<Instrument> instruments) {
        this.instruments = instruments;
    }

    public boolean addCustomInstruments(CustomSkin customSkin) {
        var parentName = customSkin.getParent();
        var parentOptional = instruments
                .stream()
                .filter(c -> c.getName().equals(parentName))
                .findFirst();

        if(parentOptional.isEmpty())
        {
            return false;
        }

        instruments.add(new CustomInstrument(customSkin,parentOptional.get()));
        return true;
    }




    public List<String> getNames() {
        return instruments.stream().map(Instrument::getName).toList();
    }

    public Optional<Instrument> getByName(String name) {
        return instruments.stream().filter(c -> c.getName().equals(name)).findFirst();
    }


}
