package jw.instruments.spigot.gameobjects.test;

import jw.fluent_api.spigot.gameobjects.api.GameObject;
import jw.fluent_api.utilites.math.collistions.HitBox;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
/*
      private Map<Integer, FretGameobject> freats;
   @Override
   public void onLocationUpdated() {
       createFreates();
   }
     private List<HitBox> hitBoxList = new ArrayList<>();
   private HitBox hitBox;

   public void setUpSymulator() {
       freats = new HashMap<>();
       modelRenderer.setCustomModel(instrumentData.getCustomModel().getItemStack());
       giveChords();
   }

   public void createFreates() {
       freats.clear();
       var loc = modelRenderer.getArmorStand().getLocation().clone();

       loc.add(-0.5, 1.35f, 0);
       var width = 0.1f;
       var offset = 0.3f;
       var height = 0.4f;
       var maxFreats = 10;
       var locStart = loc.clone();
       for (var i = 0; i < maxFreats; i++) {

           var fret = addGameComponent(new FretGameobject());
           fret.setHitboxLocation(loc);
           freats.put(maxFreats - 1 - i, fret);
           loc = loc.clone().add(width + offset, 0, 0);
       }
       var locEnd = loc.clone();

       hitBox = new HitBox(locStart, locEnd.add(0, height, width * 2));
       //  hitBox.show();
   }

   @Override
   public void onDestroy() {
       removeChords();
       hitBoxList.forEach(HitBox::hide);
       hitBox.hide();
   }
   @Override
   public boolean handlePlayerInteraction(Player player, Location location, int range) {
       if (hitBox == null) {
           return false;
       }
       FluentLogger.success("A");
       if (!hitBox.isCollider(location, range)) {
           return false;
       }
       FluentLogger.success("B");
       for (var fretClicked : freats.values()) {
           if (!fretClicked.handlePlayerInteraction(player, location, range))
               continue;

           return true;
       }
       return false;
   }
    */
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
        if (!hitBox.isCollider(location, range)) {
            return false;
        }
        for (var note : notes) {
            if (!note.getHitBox().isCollider(location, range))
                continue;
            note.onPlayerInteraction(player, location);
            return true;
        }
        return false;
    }

}
