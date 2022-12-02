package jw.instruments.core.rhythms.events;

import jw.instruments.core.data.chords.Chord;
import jw.instruments.core.data.chords.Note;
import jw.instruments.core.rhythms.timeline.Timeline;

import java.util.List;

public record ChordChangeEvent(Chord chord, Timeline timeline)
{
    public List<Note> notes()
    {
        return chord.notes();
    }
}
