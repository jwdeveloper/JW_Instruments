package jw.guitar.chords;


public record Note(int bar, int id,  float pitch, float volume) {


    @Override
    public String toString() {
        return "Note{" +
                "bar=" + bar +
                ", id=" + id +
                ", pitch=" + pitch +
                ", volume=" + volume +
                '}';
    }
}
