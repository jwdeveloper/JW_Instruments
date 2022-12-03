package jw.instruments.spigot.gameobjects;

import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Inject;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent_api.player_context.api.PlayerContext;
import jw.fluent_api.spigot.permissions.implementation.PermissionsUtility;
import jw.fluent_plugin.implementation.FluentApi;
import jw.fluent_plugin.implementation.modules.logger.FluentLogger;
import jw.fluent_plugin.implementation.modules.player_context.api.FluentPlayer;
import jw.instruments.core.data.Consts;
import jw.instruments.core.data.PluginPermissions;
import jw.instruments.core.data.instrument.InstrumentItemStack;
import jw.instruments.core.data.instrument.InstrumentItemStackObserver;
import jw.instruments.core.services.ChordService;
import jw.instruments.spigot.messages.InstrumentMessages;
import jw.instruments.spigot.gui.InstrumentViewGui;
import jw.instruments.core.rhythms.events.PlayingStyleEvent;
import jw.instruments.core.managers.PlayerChordManager;
import jw.instruments.core.services.RhythmService;
import jw.fluent_api.spigot.gameobjects.api.GameObject;
import jw.fluent_api.spigot.messages.FluentMessage;
import jw.fluent_api.utilites.messages.Emoticons;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;


@PlayerContext
@Injection
public class InstrumentPlayer extends GameObject {
    private RhythmService rhythmService;
    private PlayerChordManager chordManager;
    private InstrumentItemStackObserver instrumentObserver;
    private FluentPlayer fluentPlayer;
    @Inject
    public InstrumentPlayer(RhythmService rhythmService,
                            PlayerChordManager chordManager,
                            InstrumentItemStackObserver instrumentObserver,
                            FluentPlayer player)

    {
        this.rhythmService = rhythmService;
        this.chordManager = chordManager;
        this.instrumentObserver = instrumentObserver;
        this.fluentPlayer =player;
    }

    public void setInstrument(InstrumentItemStack instrument)
    {
        instrumentObserver.setInstrument(instrument);
        sendInfoMessage();
    }

    @Override
    public void onCreated() {
        chordManager.setDataObserver(instrumentObserver);

    }

    public void changeChord(int slot)
    {
        chordManager.setCurrentSlot(slot);
        if(instrumentObserver.getDisplayChordsName().get())
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
        msg.sendActionBar(fluentPlayer.get());
    }


    public void playChord(boolean leftClick) {

        if(!PermissionsUtility.hasOnePermission(fluentPlayer.get(), PluginPermissions.PLAY))
        {
            return;
        }

        var chord =  chordManager.getCurrentChord();
        if(chord == null)
        {
            return;
        }

        rhythmService.currentStyle()
                .play(new PlayingStyleEvent(fluentPlayer.get(),
                        chord,
                        leftClick,
                        instrumentObserver.getInstrumentName(),
                        instrumentObserver.getVolume().get()));
    }

    public void changeStyle(Player player) {

        if(!instrumentObserver.getChangeRhytmOnShift().get())
        {
            return;
        }
        var current = rhythmService.nextStyle();
        InstrumentMessages.rhythmChange(player, current);
    }

    @Override
    public void onDestroy() {

    }

    public void openGUI() {
        FluentApi.spigot()
                .playerContext()
                .find(InstrumentViewGui.class, fluentPlayer.get())
                .open(fluentPlayer.get(), instrumentObserver);
    }




    private void sendInfoMessage()
    {
        var player = fluentPlayer.get();
        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC,1,1);
        FluentMessage.message()
                .info()
                .text("Use ", ChatColor.GRAY)
                .text("/" +Consts.PLUGIN_NAMESPACE, ChatColor.AQUA)
                .text(" to edit instrument or chords ", ChatColor.GRAY)
                .send(player);
    }
}
