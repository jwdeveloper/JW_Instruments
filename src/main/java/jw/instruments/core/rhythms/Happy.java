package jw.instruments.core.rhythms;

import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent.api.desing_patterns.dependecy_injection.api.enums.LifeTime;
import jw.instruments.core.rhythms.events.PlayingStyleEvent;

import java.util.ArrayList;
import java.util.List;

@Injection(lifeTime = LifeTime.TRANSIENT)
public class Happy implements Rhythm {

    private PlayingPattern left;
    private PlayingPattern right;

    public Happy() {
        left = new PlayingPattern();
        left.add(4);
        left.add(5);
        right = new PlayingPattern();
        right.add(0, 1, 2);
    }


    @Override
    public void cancel() {

    }

    @Override
    public void play(PlayingStyleEvent event) {
        float volume = (event.volume()/100f);
        if (event.leftClick())
        {
            play(event,left.next(),volume*1.6f);
        }
        else
        {
            play(event,right.next(),volume*0.7f);
        }
    }

    private void play(PlayingStyleEvent event, int[] sounds, float volume)
    {
        for(var sound : sounds)
        {
            var notes = event.chord().notes().stream().filter(c -> c.id() == sound).toList();
            for(var note : notes)
            {

                event.getWorld().playSound(event.getLocation(),
                        getSoundName(note.id(),event.guitarType()),
                        volume,
                        note.pitch());
            }

        }
    }


    public class PlayingPattern {
        private List<int[]> patterns = new ArrayList<>();
        private int index = -1;

        public int[] next() {
            index = (index + 1) % patterns.size();
            return patterns.get(index);
        }

        public void add(int... notes) {
            patterns.add(notes);
        }
    }

}
