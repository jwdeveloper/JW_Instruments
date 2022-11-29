package jw.guitar.core.rhythms.events;

import jw.guitar.core.data.chords.Chord;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public record PlayingStyleEvent(Player player,
                                Chord chord,
                                boolean leftClick,
                                String guitarType)
{

    public Location getLocation()
    {
        return player.getLocation();
    }

    public World getWorld()
    {
        return getLocation().getWorld();
    }
}
