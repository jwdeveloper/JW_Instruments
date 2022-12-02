package jw.instruments.spigot.gameobjects;

import jw.fluent_api.spigot.permissions.implementation.PermissionsUtility;
import jw.fluent_plugin.implementation.FluentApi;
import jw.instruments.core.data.Consts;
import jw.instruments.core.data.PluginPermissions;
import jw.instruments.core.data.instument.InstrumentDataObserver;
import jw.instruments.core.services.ChordService;
import jw.instruments.spigot.messages.InstrumentMessages;
import jw.instruments.spigot.gui.InstrumentViewGui;
import jw.instruments.core.rhythms.events.PlayingStyleEvent;
import jw.instruments.core.managers.PlayerChordManager;
import jw.instruments.core.services.RhythmService;
import jw.fluent_api.spigot.gameobjects.api.GameObject;
import jw.fluent_api.spigot.messages.FluentMessage;
import jw.fluent_api.utilites.messages.Emoticons;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class InstrumentPlayer extends GameObject {
    private RhythmService rhythmService;
    private PlayerChordManager chordManager;
    @Setter
    private Player player;
    @Setter
    private InstrumentDataObserver data;

    @Override
    public void onCreated() {
        rhythmService = FluentApi.injection().findInjection(RhythmService.class);
        chordManager = FluentApi.injection().findInjection(PlayerChordManager.class);
        chordManager.setDataObserver(data);
        setDefaultChords();
        sendInfoMessage();
    }

    public void changeChord(int slot)
    {
        chordManager.setCurrentSlot(slot);
        if(data.getDisplayChordsName().get())
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
            msg.color(start).text(Emoticons.square).space()
                    .color(color);
            if(isIndex)
                msg.bold(name);
            else
                msg.text(name);
            msg.text(" ",start);
        }
        msg.sendActionBar(player);
    }


    public void playChord(boolean leftClick) {
        if(!PermissionsUtility.hasOnePermission(player, PluginPermissions.PLAY))
        {
            return;
        }
        var chord =  chordManager.getCurrentChord();
        if(chord == null)
        {
            return;
        }
        rhythmService.currentStyle()
                .play(new PlayingStyleEvent(player,
                        chord,
                        leftClick,
                        data.getInstumentName().get(),
                        data.getVolume().get()));
    }

    public void changeStyle() {

        if(!data.getChangeRhytmOnShift().get())
        {
            return;
        }
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
        FluentApi.spigot()
                .playerContext()
                .find(InstrumentViewGui.class, player)
                .open(player, data);
    }


    private void setDefaultChords()
    {
        ChordService chordService= FluentApi.injection().findInjection(ChordService.class);
        var names =  chordService.getDefaultChords().stream().map(c -> c.fullName()).toList();
        for(var i=0;i<names.size();i++)
        {
            data.setChord(i,names.get(i));
        }
    }

    private void sendInfoMessage()
    {
        FluentMessage.message()
                .info()
                .text("Use ", ChatColor.GRAY)
                .text("/" +Consts.PLUGIN_NAMESPACE, ChatColor.AQUA)
                .text(" to edit instrument or chords ", ChatColor.GRAY)
                .send(player);
    }
}
