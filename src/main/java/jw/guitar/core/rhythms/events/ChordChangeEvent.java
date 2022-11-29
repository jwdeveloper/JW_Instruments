package jw.guitar.core.rhythms.events;

import jw.guitar.core.data.chords.Chord;
import jw.guitar.core.data.chords.Note;
import jw.guitar.core.rhythms.timeline.Timeline;

import java.util.List;

public record ChordChangeEvent(Chord chord, Timeline timeline)
{
    public List<Note> notes()
    {
        return chord.notes();
    }
}
