package jw.instruments.core.rhythms.events;

import jw.instruments.core.data.chords.Note;

public record NoteEvent(Note note)
{
    @Override
    public String toString() {
        return "NoteEvent{" +
                "note=" + note.toString() +
                '}';
    }
}
