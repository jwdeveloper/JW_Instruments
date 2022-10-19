package jw.guitar.managers;


import jw.guitar.chords.Chord;
import jw.guitar.data.instument.InstrumentDataObserver;
import jw.guitar.services.ChordService;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.api.annotations.Inject;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.api.enums.LifeTime;
import lombok.Getter;
import lombok.Setter;

@Injection(lifeTime = LifeTime.TRANSIENT)
public class PlayerChordManager {
    private final ChordService chordService;

    @Setter
    private  InstrumentDataObserver dataObserver;

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
       return chordService.get(name);
    }
}
