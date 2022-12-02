package jw.instruments.core.rhythms.timeline;

import jw.instruments.core.data.chords.Note;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Timeline
{
    private Map<Integer, List<Note>> timeline;
    private int currentTick;
    private int maxTick = 0;
    @Setter
    private List<Note> notes;

    public Timeline()
    {
        timeline = new HashMap<>();
        currentTick =-1;
    }

    public void addNote(int tick, int note)
    {
        if(notes == null)
        {
            return;
        }

        var note_ = notes.get(note);
        addNote(tick,note_);
    }
    public void addNote(int tick, Note note)
    {
        if(!timeline.containsKey(tick))
        {
            var list = new ArrayList<Note>();
            list.add(note);
            timeline.put(tick,list);
        }
        if(tick > maxTick)
        {
            maxTick = tick;
        }
        timeline.get(tick).add(note);
    }
    public void addNote(int tick, Note... note)
    {
        for(var n : note)
        {
            addNote(tick,n);
        }
    }

    public void reset()
    {
        currentTick = -1;
        timeline.clear();
    }

    public void setTick(int tick)
    {
        currentTick = tick;
    }

    public boolean done()
    {
        return currentTick > maxTick;
    }

    public List<Note> next()
    {
        currentTick ++;
        if(!timeline.containsKey(currentTick))
        {
            return List.of();
        }
        return timeline.get(currentTick);
    }

}
