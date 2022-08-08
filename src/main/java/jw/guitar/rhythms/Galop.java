package jw.guitar.rhythms;

import jw.guitar.rhythms.events.PlayingStyleEvent;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.annotations.Inject;
import jw.spigot_fluent_api.fluent_logger.FluentLogger;
import jw.spigot_fluent_api.fluent_tasks.FluentTaskTimer;
import jw.spigot_fluent_api.fluent_tasks.FluentTasks;

public class Galop extends Normal {

    private FluentTaskTimer tasks;

    @Override
    public void play(PlayingStyleEvent event) {

        if(tasks != null && !event.leftClick())
        {
            tasks.cancel();
            tasks = null;
            return;
        }

        tasks = FluentTasks.taskTimer(1, (iteration, fluentTask) ->
        {
            if( tasks == null || !tasks.equals(fluentTask))
            {
                fluentTask.cancel();
                return;
            }

            switch (iteration) {
                case 0,5:
                    down(event);
                    FluentLogger.log("down");
                    break;
                case 4,6:
                    up(event);
                    FluentLogger.log("up");
                    break;
            }

        }).stopAfterIterations(7).onStop(fluentTaskTimer ->
        {
            if( tasks == null || !tasks.equals(fluentTaskTimer))
                return;

            play(event);
        }).run();
    }

    @Override
    protected void up(PlayingStyleEvent event) {

        for(var i =0;i<event.chord().notes().size()/2;i++)
        {
            var note = event.chord().notes().get(i);
            event.getWorld().playSound(event.getLocation(),
                    getSoundName(note.id(),event.guitarType()),
                    0.6f,
                    note.pitch());
        }
    }
}
