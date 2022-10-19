package jw.guitar.services;

import jw.guitar.chords.Chord;
import jw.guitar.builders.chord.ChordBuilder;
import jw.guitar.chords.Note;
import jw.guitar.data.Consts;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent_api.minecraft.messages.FluentMessage;
import jw.fluent_api.utilites.math.MathUtility;
import jw.fluent_api.utilites.messages.Emoticons;
import org.bukkit.ChatColor;

import java.util.*;

@Injection
public class ChordService {
    private Map<String, Chord> chords;
    private Map<String, String[]> chordsDisplay;
    private String[] chordNames;

    public ChordService() {
        chords = new LinkedHashMap<>();
        chordsDisplay = new HashMap<>();
        chordNames = new String[6];
        chordNames[5] = "E";
        chordNames[4] = "B";
        chordNames[3] = "G";
        chordNames[2] = "D";
        chordNames[1] = "A";
        chordNames[0] = "E";
    }

    public void add(Chord chord) {
        chords.putIfAbsent(chord.fullName(), chord);
    }

    public void add(List<Chord> chords) {
        chords.forEach(this::add);
    }

    public Chord get(String name) {
        return chords.get(name);
    }

    public Map<Integer, Chord> get(Map<Integer, String> chords) {
        var result = new HashMap<Integer, Chord>();
        for (var chordName : chords.entrySet()) {
            var chord = get(chordName.getValue());
            if (chord == null)
                continue;

            result.put(chordName.getKey(), chord);
        }
        return result;
    }

    public String[] getDisplay(Chord chord) {
        if (chordsDisplay.containsKey(chord.fullName())) {
            return chordsDisplay.get(chord.fullName());
        }

        var builder = FluentMessage.message();
        var bar = chord.fret();
        var values = chord.notes().stream().map(Note::id).toList();
        var max = Collections.max(values) + 1;
        builder.space().space();
        for (var i = bar; i < max; i++) {
            builder.text(bar + i - 1).space();
        }
        builder.newLine();

        for (var i = Consts.STRINGS - 1; i >= 0; i--) {
            var note = chord.notes().get(i);
            var index = note.finger();
            builder.text(chordNames[i]).space();
            for (var j = bar; j < max; j++) {
                if (j == index) {

                    builder.color(ChatColor.AQUA).text(Emoticons.square);
                } else {
                    builder.color(ChatColor.GRAY).text(Emoticons.square_not_filled);
                }
                builder.space();
            }
            builder.newLine();
        }

        var result = builder.toArray();
        chordsDisplay.put(chord.fullName(), result);
        return result;
    }

    public List<Chord> get() {
        return chords.values().parallelStream().toList();
    }

    public List<Chord> getDefaultChords() {
        var names = new ArrayList<String>();
        names.add("A minor");
        names.add("C major");
        names.add("G major");
        names.add("F major");
        names.add("E major");

        names.add("D major");
        names.add("E minor");
        names.add("B minor");
        var result = new ArrayList<Chord>();
        for (var name : names) {
            result.add(get(name));
        }
        return result;
    }

    public List<Chord> getByKey(String key) {
        return chords.values().stream().filter(c -> c.key().equals(key)).toList();
    }


    public Chord generateRandomNoise() {
        var builder = ChordBuilder.create("noise");
        for (var i = 0; i < MathUtility.getRandom(3, 10); i++) {

            var bar = MathUtility.getRandom(0, 10);
            var note = MathUtility.getRandom(0, 5);
            var sound = MathUtility.getRandom(10, 200);
            builder.addNote(bar, note, sound * 0.01f);
        }
        return builder.build();
    }

}
