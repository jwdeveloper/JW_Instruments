package jw.instruments.core.rhythms;

import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent.api.desing_patterns.dependecy_injection.api.enums.LifeTime;
import jw.instruments.core.data.chords.Note;
import jw.instruments.core.rhythms.events.NoteEvent;
import jw.instruments.core.rhythms.events.PlayingStyleEvent;
import jw.instruments.core.rhythms.timeline.Timeline;
import jw.fluent.api.spigot.tasks.SimpleTaskTimer;
import jw.fluent.plugin.implementation.modules.tasks.FluentTasks;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@Injection(lifeTime = LifeTime.TRANSIENT)
public class Fingering implements Rhythm {

    private boolean isReady = true;
    private SimpleTaskTimer taskTimer;
    private PlayingStyleEvent queuedEvent;
    private PlayingStyleEvent currentEvent;

    private Set<Consumer<NoteEvent>> events = new HashSet<>();
    private Timeline timeline;
    private Integer[] pattern;
    public Fingering() {
        timeline = new Timeline();
    }

    @Override
    public void onEvent(Consumer<NoteEvent> event) {
        events.add(event);
    }

    @Override
    public void cancel() {

    }

    @Override
    public void emitEvent(NoteEvent noteEvent) {

        for (var event : events) {
            event.accept(noteEvent);
        }
    }

    @Override
    public void play(PlayingStyleEvent event) {

        if (!event.leftClick() && !isReady) {
            taskTimer.cancel();
            queuedEvent = null;
            currentEvent = null;
            isReady = true;
            return;
        }

        if (!isReady) {
            queuedEvent = event;
            return;
        }

        setEvent(event);
        taskTimer = FluentTasks.taskTimer(2, (iteration, task) ->
                {

                    if (timeline.done())
                    {
                        if (queuedEvent != null) {
                            task.setIteration(0);
                            setEvent(queuedEvent);
                            queuedEvent = null;
                        } else {
                            isReady = true;
                            task.cancel();
                            return;
                        }
                    }
                    var notes = timeline.next();
                    float volume = (event.volume()/100f);
                    for (var note : notes) {
                        currentEvent
                                .getWorld()
                                .playSound(currentEvent.getLocation(),
                                        getSoundName(note.id(), currentEvent.guitarType()),
                                        volume,
                                        note.pitch());
                        emitEvent(new NoteEvent(note));
                    }
                })
                .run();
        isReady = false;
    }
    public void setEvent(PlayingStyleEvent event) {
        currentEvent = event;
        timeline.reset();
        var notes = event.chord().notes();

        timeline.addNote(0, notes.get(1));
        timeline.addNote(2, notes.get(2));
        timeline.addNote(3, notes.get(3));
        timeline.addNote(4, notes.get(4));
        timeline.addNote(6, notes.get(5));
        timeline.addNote(8, notes.get(4));
        timeline.addNote(10, notes.get(3));
    }

    public void setPattern(List<Note> notes, Timeline timeline)
    {

    }
}
