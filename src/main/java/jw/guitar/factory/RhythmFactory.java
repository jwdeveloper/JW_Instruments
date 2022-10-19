package jw.guitar.factory;

import jw.guitar.rhythms.Rhythm;
import jw.guitar.builders.rhythm.RhythmBuilder;
import jw.spigot_fluent_api.fluent_logger.FluentLogger;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

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
                .onNote(noteEvent ->
                {
                    FluentLogger.log("siema",noteEvent.toString());
                })
                .setName("Rising_Sun")
                .build();
    }
}
