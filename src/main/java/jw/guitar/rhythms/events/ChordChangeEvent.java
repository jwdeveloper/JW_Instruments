package jw.guitar.rhythms.events;

import jw.guitar.chords.Chord;
import jw.guitar.chords.Note;
import jw.guitar.rhythms.timeline.Timeline;

import java.util.List;

public record ChordChangeEvent(Chord chord, Timeline timeline)
{
    public List<Note> notes()
    {
        return chord.notes();
    }
}
