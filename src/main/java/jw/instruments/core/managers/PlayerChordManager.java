package jw.instruments.core.managers;


import jw.instruments.core.data.chords.Chord;
import jw.instruments.core.data.instrument.InstrumentItemStackObserver;
import jw.instruments.core.services.ChordService;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Inject;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent_api.desing_patterns.dependecy_injection.api.enums.LifeTime;
import lombok.Getter;
import lombok.Setter;

@Injection(lifeTime = LifeTime.TRANSIENT)
public class PlayerChordManager {
    private final ChordService chordService;
    @Setter
    private InstrumentItemStackObserver dataObserver;

    @Setter
    @Getter
    private Integer currentSlot = 0;

    @Inject
    public PlayerChordManager(ChordService chordService) {
        this.chordService = chordService;
    }
    public String[] names()
    {
        return dataObserver.getChords().get();
    }
    public Chord getCurrentChord()
    {
       var name =dataObserver.getChords().get()[currentSlot];
       return chordService.find(name);
    }




}
