package jw.guitar.gameobjects;

import jw.spigot_fluent_api.fluent_gameobjects.api.GameObject;
import jw.spigot_fluent_api.fluent_logger.FluentLogger;
import jw.spigot_fluent_api.utilites.math.collistions.HitBox;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FretGameobject extends GameObject {

    private HitBox hitBox;
    @Getter
    private NoteGameObject[] notes;

    private double width = 0.1f;
    private double offset = 0.3f;
    private double height = 0.4f;


    private List<HitBox> createHitboxes(Location location) {
        var result = new ArrayList<HitBox>();
        var min = location.clone().add(0, 0, width);
        var max = location.clone().add(width, height, width * 2);
        hitBox = new HitBox(min, max);

        min = hitBox.getMin();
        max = hitBox.getMax();
        var distance = (max.getY() - min.getY()) / 6.0f;
        var noteMin = min.clone();
        var noteMax = new Location(noteMin.getWorld(), max.getX(), noteMin.getY() + distance, max.getZ());
        for (var i = 0; i < 6; i++) {
            var hitBox = new HitBox(noteMin, noteMax);
           // hitBox.show();

            noteMin = noteMin.add(0, distance, 0);
            noteMax = noteMax.add(0, distance, 0);
            result.add(hitBox);
        }
        return result;
    }

    public void setHitboxLocation(Location location) {
        var hitboxes = createHitboxes(location);
        notes = new NoteGameObject[6];
        for (var i = 0; i < 6; i++) {
            notes[i] = addGameComponent(new NoteGameObject());
            notes[i].setHitBox(hitboxes.get(i));
        }
    }

    @Override
    public void onDestroy() {
        if(hitBox!=null)
            hitBox.hide();;
    }

    @Override
    public boolean handlePlayerInteraction(Player player, Location location, int range) {
        if (hitBox == null) {
            return false;
        }
        FluentLogger.success("C");
        if (!hitBox.isCollider(location, range)) {
            return false;
        }
        FluentLogger.success("D");
        for (var note : notes) {
            if (!note.getHitBox().isCollider(location, range))
                continue;
            FluentLogger.success("note click", note.toString());
            note.onPlayerInteraction(player, location);
            return true;
        }
        return false;
    }

}
