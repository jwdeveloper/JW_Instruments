package jw.guitar.chords;

import jw.guitar.factory.PithFactory;

import java.util.ArrayList;
import java.util.List;

public class ChordBuilder {
    private final List<Note> notes;
    private String key;
    private String suffix;


    ChordBuilder(String key, String suffix) {
        notes = new ArrayList<>();
        this.key = key;
        this.suffix = suffix;
    }

    public static ChordBuilder create(String name) {
        return new ChordBuilder(name, "");
    }

    public static ChordBuilder create(String key, String suffix) {
        return new ChordBuilder(key, suffix);
    }

    public ChordBuilder addNote(int bar, int id) {
        return addNote(bar, id, 1);
    }

    public ChordBuilder addNote(int bar, int id, float volume) {
        bar = Math.max(bar, 0);
        var pitch = getPitch(bar, id);
        notes.add(new Note(bar, id, pitch, volume));
        return this;
    }


    private float getPitch(int bar, int id) {
        var fretsTunings = PithFactory.getPitch();
        var fret = fretsTunings.get(id);
        return (float) (fret.getTunnings()[bar] / fret.getTunnings()[0]);
    }


    public ChordBuilder addE1(int bar) {
        return addNote(bar, 0);
    }

    public ChordBuilder addE1() {
        return addE1(0);
    }

    public ChordBuilder addB2(int bar) {
        return addNote(bar, 1);
    }

    public ChordBuilder addB2() {
        return addB2(0);
    }

    public ChordBuilder addG3(int bar) {
        return addNote(bar, 2);
    }

    public ChordBuilder addG3() {
        return addG3(0);
    }

    public ChordBuilder addD4(int bar) {
        return addNote(bar, 3);
    }

    public ChordBuilder addD4() {
        return addD4(0);
    }

    public ChordBuilder addA5(int bar) {
        return addNote(bar, 4);
    }

    public ChordBuilder addA5() {
        return addA5(0);
    }

    public ChordBuilder addE6(int bar) {
        return addNote(bar, 5);
    }

    public ChordBuilder addE6() {
        return addE6(0);
    }

    public ChordBuilder otherDefault(int bar) {
        List<Integer> used = new ArrayList<>();
        for (var note : notes) {
            used.add(note.id());
        }

        for (var i = 0; i < 6; i++) {
            if (used.contains(i)) {
                continue;
            }
            addNote(bar, i, 0.5f);
        }
        return this;
    }

    public ChordBuilder otherDefault() {
        return otherDefault(0);
    }


    public Chord build() {
        return new Chord(key, suffix, notes);
    }

}
