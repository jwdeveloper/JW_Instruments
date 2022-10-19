package jw.guitar.rhythms.events;

import jw.guitar.chords.Note;

public record NoteEvent(Note note)
{
    @Override
    public String toString() {
        return "NoteEvent{" +
                "note=" + note.toString() +
                '}';
    }
}
