package jw.guitar.core.rhythms.events;

import jw.guitar.core.data.chords.Note;

public record NoteEvent(Note note)
{
    @Override
    public String toString() {
        return "NoteEvent{" +
                "note=" + note.toString() +
                '}';
    }
}
