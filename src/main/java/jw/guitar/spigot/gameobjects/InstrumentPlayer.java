package jw.guitar.spigot.gameobjects;

import jw.fluent_plugin.implementation.FluentApi;
import jw.guitar.core.data.instument.InstrumentDataObserver;
import jw.guitar.spigot.messages.InstrumentMessages;
import jw.guitar.spigot.gui.InstrumentViewGui;
import jw.guitar.core.rhythms.events.PlayingStyleEvent;
import jw.guitar.core.managers.PlayerChordManager;
import jw.guitar.core.services.RhythmService;
import jw.fluent_api.spigot.gameobjects.api.GameObject;
import jw.fluent_api.spigot.messages.FluentMessage;
import jw.fluent_api.utilites.messages.Emoticons;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


public class InstrumentPlayer extends GameObject {
    private RhythmService rhythmService;
    private PlayerChordManager chordManager;
    @Setter
    private Player player;
    @Setter
    private InstrumentDataObserver data;

    public static InstrumentPlayer create(Player player, InstrumentDataObserver instrumentData) {
        final var go = new InstrumentPlayer();
        go.setPlayer(player);
        go.setData(instrumentData);
        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC,1,1);
        return go;
    }

    @Override
    public void onCreated() {
        rhythmService = FluentApi.injection().findInjection(RhythmService.class);
        chordManager = FluentApi.injection().findInjection(PlayerChordManager.class);
        chordManager.setDataObserver(data);

    }

    public void changeChord(int slot)
    {
        chordManager.setCurrentSlot(slot);
        if(data.getDisplayNames().get())
        {
            displayChordsName();
        }
    }

    private void displayChordsName()
    {
        var msg =  FluentMessage.message();
        var names = chordManager.names();
        var slot = chordManager.getCurrentSlot();
        for(var i =0 ;i< names.length;i++)
        {
            var isIndex = i == slot;
            var name = names[i] == null? Emoticons.note :names[i];
            var start = isIndex?ChatColor.BOLD:ChatColor.RESET;
            var color = isIndex?ChatColor.AQUA:ChatColor.GRAY;
            msg.color(start).text(" | ",color);
            if(isIndex)
                msg.bold(name);
            else
                msg.text(name);
            msg.text(" | ",color);
        }
        msg.sendActionBar(player);
    }


    public void playChord(boolean leftClick) {
        var chord =  chordManager.getCurrentChord();
        if(chord == null)
        {
            return;
        }
        rhythmService.currentStyle()
                .play(new PlayingStyleEvent(player,
                        chord,
                        leftClick,
                        data.getInstumentName().get()));
    }

    public void changeStyle() {
        var current = rhythmService.nextStyle();
        InstrumentMessages.rhythmChange(player, current);
    }

    @Override
    public void onDestroy() {

    }

    public void makeNoise() {
        rhythmService.playNoise(player, data.getInstumentName().get());
    }

    public void openGUI() {
        FluentApi.spigot().playerContext().find(InstrumentViewGui.class, player)
                .open(player, data);
    }



}
