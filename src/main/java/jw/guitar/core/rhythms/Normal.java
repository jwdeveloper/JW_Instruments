package jw.guitar.core.rhythms;


import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent_api.desing_patterns.dependecy_injection.api.enums.LifeTime;
import jw.fluent_plugin.implementation.FluentApi;
import jw.guitar.core.rhythms.events.NoteEvent;
import jw.guitar.core.rhythms.events.PlayingStyleEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@Injection(lifeTime = LifeTime.TRANSIENT)
public class Normal implements Rhythm {

    private Set<Consumer<NoteEvent>> events = new HashSet<>();

    @Override
    public void onEvent(Consumer<NoteEvent> event) {
        events.add(event);
    }

    @Override
    public void cancel() {

    }

    @Override
    public void emitEvent(NoteEvent noteEvent) {

        for(var event : events)
        {
            event.accept(noteEvent);
        }
    }
    @Override
    public void play(PlayingStyleEvent event) {
        if(event.leftClick())
            down(event);
        else
            up(event);
    }

    protected void down(PlayingStyleEvent event)
    {
        for (var note : event.chord().notes()) {
            event.getWorld().playSound(
                    event.getLocation(),
                    getSoundName(note.id(),event.guitarType()),
                    3,
                    note.pitch());
            FluentApi.logger().log("note",getSoundName(note.id(),event.guitarType()),
                    "id",
                    note.id(),
                    "fret",
                    event.chord().fret(),
                    "Pith",note.pitch());
            emitEvent(new NoteEvent(note));
        }
    }

    protected void up(PlayingStyleEvent event)
    {
        for (var note : event.chord().notes()) {
            event.getWorld().playSound(event.getLocation(),
                    getSoundName(note.id(),event.guitarType()),
                    0.3f,
                    note.pitch());
            emitEvent(new NoteEvent(note));
        }
    }
}
