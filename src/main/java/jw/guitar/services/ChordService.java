package jw.guitar.services;

import jw.guitar.chords.Chord;
import jw.guitar.chords.ChordBuilder;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.annotations.Injection;
import jw.spigot_fluent_api.utilites.math.MathUtility;

import java.util.*;

@Injection
public class ChordService {
    private Map<String, Chord> chords;

    public ChordService()
    {
        chords = new LinkedHashMap<>();
    }


    public void add(Chord chord)
    {
         chords.putIfAbsent(chord.fullName(),chord);
    }

    public void add(List<Chord> chords)
    {
        chords.forEach(this::add);
    }

    public Chord get(String name)
    {
        return chords.get(name);
    }

    public Map<Integer,Chord> get(Map<Integer,String> chords)
    {
        var result = new HashMap<Integer, Chord>();
        for(var chordName : chords.entrySet())
        {
            var chord = get(chordName.getValue());
            if(chord == null)
                continue;

            result.put(chordName.getKey(),chord);
        }
        return result;
    }

    public List<Chord> get()
    {
        return chords.values().parallelStream().toList();
    }

    public List<Chord> getDefaultChords()
    {
        var names = new ArrayList<String>();
        names.add("A minor");
        names.add("C major");
        names.add("G major");
        names.add("F major");
        names.add("E major");

        names.add("D major");
        names.add("E minor");
        names.add("B minor");
        var result = new ArrayList<Chord>();
        for(var name : names)
        {
            result.add(get(name));
        }
        return result;
    }

    public List<Chord> getByKey(String key)
    {
         return chords.values().stream().filter(c -> c.key().equals(key)).toList();
    }


    public  Chord generateRandomNoise() {
        var builder =  ChordBuilder.create("noise");
        for (var i = 0; i < MathUtility.getRandom(3, 10); i++) {

            var bar = MathUtility.getRandom(0, 10);
            var note = MathUtility.getRandom(0,5);
            var sound = MathUtility.getRandom(10,200);
            builder.addNote(bar,note, sound*0.01f);
        }
        return builder.build();
    }

}
