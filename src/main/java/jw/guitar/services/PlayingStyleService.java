package jw.guitar.services;

import jw.guitar.rhythms.*;
import jw.guitar.rhythms.events.NoteEvent;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.annotations.Inject;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.annotations.Injection;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.enums.LifeTime;

import java.util.List;
import java.util.function.Consumer;

@Injection(lifeTime = LifeTime.TRANSIENT)
public class PlayingStyleService {
    private List<Rhythm> playingStyles;
    private int current;

    @Inject
    public PlayingStyleService(List<Rhythm> playingStyles) {
        this.playingStyles = playingStyles;
        current = 0;
    }

    public void onEvent(Consumer<NoteEvent> noteEventConsumer) {
        for (var rythm : playingStyles) {
            rythm.onEvent(noteEventConsumer);
        }
    }

    public Rhythm nextStyle() {
        current = (current + 1) % playingStyles.size();
        return currentStyle();
    }

    public Rhythm currentStyle() {
        return playingStyles.get(current);
    }

}
