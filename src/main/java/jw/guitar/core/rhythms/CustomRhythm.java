package jw.guitar.core.rhythms;

import jw.fluent_plugin.implementation.FluentApi;
import jw.guitar.core.builders.rhythm.RhythmModel;
import jw.guitar.core.rhythms.events.ChordChangeEvent;
import jw.guitar.core.rhythms.events.NoteEvent;
import jw.guitar.core.rhythms.events.PlayingStyleEvent;
import jw.guitar.core.rhythms.timeline.Timeline;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.IgnoreInjection;
import jw.fluent_api.spigot.tasks.FluentTaskTimer;
import jw.fluent_api.spigot.tasks.FluentTasks;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@IgnoreInjection
public class CustomRhythm implements Rhythm {

    private boolean isReady = true;
    private FluentTaskTimer taskTimer;
    private PlayingStyleEvent queuedEvent;
    private PlayingStyleEvent currentEvent;

    private final Set<Consumer<NoteEvent>> onNotes;
    private final Timeline timeline;
    private final Consumer<ChordChangeEvent> onChordChange;
    private final RhythmModel model;

    public CustomRhythm(RhythmModel model) {
        timeline = new Timeline();
        onNotes = new HashSet<>();
        onNotes.addAll(model.getEvents());
        onChordChange = model.getOnChordChanged();
        this.model = model;
    }

    @Override
    public String getName() {
        return model.getName();
    }

    @Override
    public void onEvent(Consumer<NoteEvent> event) {
        onNotes.add(event);
    }

    @Override
    public void cancel() {
        if (taskTimer == null) {
            return;
        }
        currentEvent = null;
        queuedEvent = null;
        timeline.reset();
        taskTimer.cancel();
    }

    @Override
    public void emitEvent(NoteEvent noteEvent) {
        for (var event : onNotes) {
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
        taskTimer = FluentTasks.taskTimer(model.getSpeed(), (iteration, task) ->
                {
                    if (timeline.done()) {
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
                    for (var note : timeline.next()) {
                        //Bukkit.getOnlinePlayers().forEach(c -> c.playSound(c.getLocation(),"minecraft:acoustic3",1,1));
                        FluentApi.logger().info("Name",getSoundName(note.id(), currentEvent.guitarType()));
                        currentEvent
                                .getWorld()
                                .playSound(currentEvent.player().getLocation(),
                                        getSoundName(note.id(), currentEvent.guitarType()),
                                        1,
                                        note.pitch());
                        emitEvent(new NoteEvent(note));
                    }
                })
                .run();
        isReady = false;
    }

    private void setEvent(PlayingStyleEvent event) {
        currentEvent = event;
        timeline.reset();
        timeline.setNotes(event.chord().notes());
        onChordChange.accept(new ChordChangeEvent(event.chord(), timeline));
    }

}
