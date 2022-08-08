package jw.guitar.services;


import jw.guitar.gameobjects.instuments.Instrument;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.annotations.Inject;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.annotations.Injection;
import jw.spigot_fluent_api.fluent_logger.FluentLogger;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Injection
public class InstrumentService {
    @Getter
    private final List<Instrument> instruments;

    @Inject
    public InstrumentService(List<Instrument> instruments) {
        FluentLogger.log("Siema ",instruments.size());
        this.instruments = instruments;
        instruments.addAll(loadCustomInstruments());
        getNames().forEach(e -> FluentLogger.log("Name",e));
    }

    private List<Instrument> loadCustomInstruments() {
        return new ArrayList<>();
    }


    public List<String> getNames() {
        return instruments.stream().map(Instrument::getName).toList();
    }

    public Optional<Instrument> getByName(String name) {
        return instruments.stream().filter(c -> c.getName().equals(name)).findFirst();
    }

}
