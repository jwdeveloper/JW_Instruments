package jw.instruments.core.rhythms;


import jw.instruments.core.rhythms.events.NoteEvent;
import jw.instruments.core.rhythms.events.PlayingStyleEvent;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Injection;

import java.util.function.Consumer;

@Injection
public interface Rhythm {

    default String getName() {

        return  getClass().getSimpleName();
    }
    default void onEvent(Consumer<NoteEvent> event)
    {

    }

    void cancel();

    default String getSoundName(int noteId, String guitarName)
    {
        noteId +=1;
        return "minecraft:"+guitarName+noteId;
    }

    default void emitEvent(NoteEvent noteEvent){};

    void play(PlayingStyleEvent event);
}
