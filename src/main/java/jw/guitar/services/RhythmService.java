package jw.guitar.services;

import jw.guitar.factory.RhythmFactory;
import jw.guitar.rhythms.*;
import jw.guitar.rhythms.events.NoteEvent;
import jw.guitar.rhythms.events.PlayingStyleEvent;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Inject;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent_api.desing_patterns.dependecy_injection.api.enums.LifeTime;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;

@Injection(lifeTime = LifeTime.TRANSIENT)
public class RhythmService {
    private final List<Rhythm> playingStyles;
    private final ChordService chordService;
    private int current =0;

    @Inject
    public RhythmService(List<Rhythm> playingStyles, ChordService chordService) {
        this.playingStyles = playingStyles;
        this.chordService = chordService;
        playingStyles.add(RhythmFactory.RisingSun());
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

    public void playNoise(Player player, String sound)
    {
        new Normal().play(
                new PlayingStyleEvent(
                        player,
                        chordService.generateRandomNoise(),
                        true,sound));
    }

    public Rhythm currentStyle() {
        return playingStyles.get(current);
    }

}
