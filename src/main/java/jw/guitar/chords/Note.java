package jw.guitar.chords;


public record Note(int finger, int id, float pitch, float volume) {


    @Override
    public String toString() {
        return "Note{" +
                "finger=" + finger +
                ", id=" + id +
                ", pitch=" + pitch +
                ", volume=" + volume +
                '}';
    }
}
