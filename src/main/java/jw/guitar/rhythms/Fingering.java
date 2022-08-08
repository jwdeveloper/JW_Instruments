package jw.guitar.rhythms;

import jw.guitar.rhythms.events.NoteEvent;
import jw.guitar.rhythms.events.PlayingStyleEvent;
import jw.spigot_fluent_api.fluent_tasks.FluentTaskTimer;
import jw.spigot_fluent_api.fluent_tasks.FluentTasks;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Fingering implements Rhythm {

    private boolean isReady = true;
    private FluentTaskTimer taskTimer;
    private PlayingStyleEvent queuedEvent;

    private Set<Consumer<NoteEvent>> events = new HashSet<>();

    @Override
    public void onEvent(Consumer<NoteEvent> event) {
        events.add(event);
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

        if (!event.leftClick() && !isReady) {
            taskTimer.cancel();
            queuedEvent = null;
            return;
        }

        if (!isReady) {
            queuedEvent = event;
            return;
        }

        if (queuedEvent != null) {
            event = queuedEvent;
            queuedEvent = null;
        }

        var chords = event.chord().notes();
        var time = chords.size() * 2;
        var halftime = time / 2;

        PlayingStyleEvent finalEvent = event;

        taskTimer = FluentTasks.taskTimer(5, (iteration, task) ->
                {
                    var index = iteration;
                    if (iteration >= halftime) {
                        index = 4 - (iteration - halftime);
                        if (index == 0) {
                            task.cancel();
                            return;
                        }
                    }
                    var note = chords.get(index);
                    finalEvent
                            .getWorld()
                            .playSound(finalEvent.getLocation(),
                                    getSoundName(note.id(), finalEvent.guitarType()),
                                    1,
                                    note.pitch());
                    emitEvent(new NoteEvent(note));
                })
                .stopAfterIterations(time)
                .onStop(fluentTaskTimer ->
                {
                    isReady = true;
                    if (queuedEvent != null) {
                        play(queuedEvent);
                    }
                })
                .run();
        isReady = false;
    }
}
