package jw.guitar.gameobjects;

import jw.guitar.data.instument.InstrumentData;
import jw.guitar.gameobjects.instuments.Instrument;
import jw.guitar.gui.InstrumentViewGui;
import jw.guitar.rhythms.Normal;
import jw.guitar.rhythms.events.PlayingStyleEvent;
import jw.guitar.services.ChordService;
import jw.guitar.services.PlayingStyleService;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.FluentInjection;
import jw.spigot_fluent_api.fluent_gameobjects.api.GameObject;
import jw.spigot_fluent_api.fluent_gameobjects.api.ModelRenderer;
import jw.spigot_fluent_api.fluent_logger.FluentLogger;
import jw.spigot_fluent_api.fluent_message.FluentMessage;
import jw.spigot_fluent_api.utilites.math.collistions.HitBox;
import jw.spigot_fluent_api.utilites.messages.Emoticons;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class InstrumentPlayer extends GameObject {
    private ChordService chordService;
    private PlayingStyleService playingStyleService;
    private ModelRenderer modelRenderer;
    private Map<Integer, FretGameobject> freats;


    public static InstrumentPlayer create(Player player, InstrumentData instrumentData) {
        final var go = new InstrumentPlayer();
        go.setPlayer(player);
        go.setInstrumentData(instrumentData);
        return go;
    }

    @Setter
    private Player player;

    @Setter
    private InstrumentData instrumentData;


    @Override
    public void onCreated() {

        modelRenderer = addGameComponent(new ModelRenderer());
        chordService = FluentInjection.getInjection(ChordService.class);
        playingStyleService = FluentInjection.getInjection(PlayingStyleService.class);

        playingStyleService.onEvent(noteEvent ->
        {
            var bar = noteEvent.note().bar();
            if(!freats.containsKey(bar))
            {
               FluentLogger.log("freat not found ",bar);
               return;
            }
            var id = noteEvent.note().id();
           freats.get(bar).getNotes()[id].show();
        });
        freats = new HashMap<>();
        modelRenderer.setCustomModel(instrumentData.getCustomModel().getItemStack());
        giveChords();

    }

    private List<HitBox> hitBoxList = new ArrayList<>();

    @Override
    public void onLocationUpdated() {
        createFreates();
    }

    private HitBox hitBox;


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
            freats.put(maxFreats-1 - i, fret);
            loc = loc.clone().add(width + offset, 0, 0);
        }
        var locEnd = loc.clone();

        hitBox = new HitBox(locStart, locEnd.add(0, height, width * 2));
     //  hitBox.show();
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


    @Override
    public void onDestroy() {
        removeChords();
        hitBoxList.forEach(hitBox -> hitBox.hide());
        hitBox.hide();
    }


    public void changeStyle() {
        var current = playingStyleService.nextStyle();
        FluentMessage.message()
                .inBrackets("Style has been changed", ChatColor.AQUA).newLine()
                .color(ChatColor.GRAY).withPrefix(current.getName(), Emoticons.arrowRight)
                .send(player);
    }

    public void playChord(PlayerInteractEvent event) {
        FluentLogger.log("Handle start");
        var loc = event.getPlayer().getLocation();
        var item = event.getPlayer().getInventory().getItemInMainHand();

        if (item == null) {
            return;
        }

        if (!item.getType().equals(Material.STRING)) {
            return;
        }

        var leftClick = true;
        var action = event.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            leftClick = false;
        }
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            leftClick = true;
        }

        var name = item.getItemMeta().getDisplayName();
        var chord = chordService.get(name);
        event.setCancelled(true);
        playingStyleService.currentStyle().play(new PlayingStyleEvent(event.getPlayer(),
                chord,
                leftClick,
                instrumentData.getSound()));
        FluentLogger.log("Done");
    }

    public void makeNoise() {
        new Normal().play(
                new PlayingStyleEvent(
                        player,
                        chordService.generateRandomNoise(),
                        true, instrumentData.getSound()));
    }

    private void giveChords() {
        var inv = player.getInventory();
        var chords = chordService.get(instrumentData.getChords());

        for (var chordData : chords.entrySet()) {
            inv.setItem(chordData.getKey(), chordData.getValue().getItemStack());
        }

        var chor = chordService.getDefaultChords();
        for (var i = 0; i < 8; i++) {
            var item = inv.getItem(i);
            if (item != null) {
                FluentLogger.log("set remove", inv.getItem(i).getItemMeta().getDisplayName());
            }
            if (Instrument.isInstrument(item)) {
                continue;
            }
            inv.setItem(i, chor.get(i).getItemStack());
        }

    }

    private void removeChords() {
        FluentLogger.log("chords remove");
        var inv = player.getInventory();
        var chords = chordService.get(instrumentData.getChords());
        for (var chordData : chords.entrySet()) {
            inv.setItem(chordData.getKey(), new ItemStack(Material.AIR));
        }
    }

    public void openGUI() {
        FluentInjection.getPlayerInjection(InstrumentViewGui.class, player).open(player, instrumentData);
    }

}
