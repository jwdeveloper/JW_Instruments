package jw.instruments.core.factory;

import jw.fluent_plugin.implementation.FluentApi;
import jw.instruments.core.rhythms.Rhythm;
import jw.instruments.core.builders.rhythm.RhythmBuilder;

public class RhythmFactory {
    public static Rhythm RisingSun() {
        return RhythmBuilder
                .create()
                .onChordChanged(event ->
                {
                    final var timeline = event.timeline();
                    timeline.addNote(0, 1);
                    timeline.addNote(2, 2);
                    timeline.addNote(3, 3);
                    timeline.addNote(4, 4);
                    timeline.addNote(6, 5);
                    timeline.addNote(8, 4);
                    timeline.addNote(10, 3);

                })
                .setSpeed(2)
                .setName("Rising_Sun")
                .build();
    }
}
