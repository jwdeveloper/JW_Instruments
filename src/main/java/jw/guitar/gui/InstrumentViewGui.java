package jw.guitar.gui;

import jw.guitar.chords.Chord;
import jw.guitar.data.instument.InstrumentData;
import jw.guitar.data.songs.Song;
import jw.guitar.factory.ButtonsFactory;
import jw.guitar.gui.songs.SongsFormGui;
import jw.guitar.gui.songs.SongsPickerGui;
import jw.guitar.services.ChordService;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.annotations.Inject;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.annotations.Injection;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.enums.LifeTime;
import jw.spigot_fluent_api.fluent_gui.button.ButtonUI;
import jw.spigot_fluent_api.fluent_gui.enums.ButtonType;
import jw.spigot_fluent_api.fluent_gui.implementation.chest_ui.ChestUI;
import jw.spigot_fluent_api.utilites.java.JavaUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;


@Injection(lifeTime = LifeTime.TRANSIENT)
public class InstrumentViewGui extends ChestUI {

    private final SongsFormGui songsFormGui;
    private final SongsPickerGui songsPickerGui;
    private final ChordPickerGui chordPickerGui;
    private final ChordService chordService;
    private Map<Integer, ButtonUI> chordsButtons;

    private InstrumentData instrumentData;

    @Inject
    public InstrumentViewGui(ChordPickerGui chordSearchGui,
                             SongsPickerGui songsPickerGui,
                             SongsFormGui songsFormGui,
                             ChordService chordService) {
        super("guitar view", 6);
        this.songsFormGui = songsFormGui;
        this.songsPickerGui = songsPickerGui;
        this.chordPickerGui = chordSearchGui;
        this.chordService = chordService;
        this.chordsButtons = new HashMap<>();
        instrumentData = new InstrumentData();
    }

    @Override
    public void onInitialize() {
        chordPickerGui.setParent(this);
        songsFormGui.setParent(this);
        chordsButtons = chordsButtons();
        setBorderMaterial(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        ButtonUI.factory()
                .goBackButton(this, getParent())
                .buildAndAdd(this);

        ButtonUI.builder()
                .setMaterial(Material.WRITABLE_BOOK)
                .setTitlePrimary("save chords to song")
                .setLocation(0, 5)
                .setOnClick((player, button) ->
                {
                    songsFormGui.openInsert(player, instrumentData.getChords());
                }).buildAndAdd(this);


        ButtonUI.builder()
                .setMaterial(Material.CHEST)
                .setTitlePrimary("load chords from song")
                .setLocation(0, 7)
                .setOnClick((player, button) ->
                {
                    songsPickerGui.onContentClick((player1, button1) ->
                    {
                        var song = button1.<Song>getDataContext();
                        var chords = song.getChords();
                        instrumentData.getChords().clear();
                        instrumentData.getChords().putAll(chords);
                        refreshChords();
                        open(player);
                    });
                    songsPickerGui.open(player);
                }).buildAndAdd(this);
    }


    public void open(Player player, InstrumentData instrumentData)
    {
        this.instrumentData = instrumentData;
        open(player);
    }


    private void refreshChords()
    {
        var chords = instrumentData.getChords();
        for(var i =0;i<INVENTORY_WIDTH;i++)
        {
            var chordName = chords.getOrDefault(i, JavaUtils.EMPTY_STRING);
            var button = chordsButtons.get(i);
            if(chordName.equals(JavaUtils.EMPTY_STRING))
            {
                button.setTitlePrimary("Chord place holder");
                button.setMaterial(Material.GRAY_STAINED_GLASS_PANE);
            }
            else
            {
                var chord = chordService.get(chordName);
                button.setTitlePrimary(chord.fullName());
                button.setCustomMaterial(chord.getItemStack().getType(), chord.getCustomId());
            }
        }
    }

    private Map<Integer, ButtonUI> chordsButtons() {
        var result = new HashMap<Integer, ButtonUI>();
        var buttons = ButtonsFactory.createChordsButtons();
        for (var i = 0; i < INVENTORY_WIDTH; i++) {
            final var slot = i;
            final var builder = buttons.get(i);
            final var btn = builder.setOnClick((player, button) ->
                    {
                        chordPickerGui.onContentClickDynamic((p, b) ->
                        {
                            var chord = b.<Chord>getDataContext();
                            button.setTitlePrimary(chord.fullName());
                            button.setCustomMaterial(chord.getItemStack().getType(), chord.getCustomId());
                            instrumentData.getChords().putIfAbsent(slot,chord.fullName());
                            open(player);
                        });
                        chordPickerGui.open(player);
                    })
                    .setOnShiftClick((player, button) ->
                    {
                        if (!instrumentData.getChords().containsKey(slot)) {
                            return;
                        }
                        instrumentData.getChords().remove(slot);
                        button.setTitlePrimary("Chord place holder");
                        button.setMaterial(Material.GRAY_STAINED_GLASS_PANE);
                        refreshButton(button);
                    })
                    .buildAndAdd(this);
            result.put(slot, btn);
        }
        return result;
    }

    @Override
    public void setBorderMaterial(Material material) {
        ButtonUI button;
        for (int i = 0; i < INVENTORY_WIDTH; i++) {

            for (int j = 0; j < getHeight(); j++) {
                if (j == 1) {
                    continue;
                }
                if (i == 0 || j == 0 || j == getHeight() - 1 || i == INVENTORY_WIDTH - 1 || j == 2) {
                    button = getButton(j, i);
                    if (button == null) {
                        button = ButtonUI.builder()
                                .setMaterial(material)
                                .setButtonType(ButtonType.BACKGROUND)
                                .setTitle(" ")
                                .setLocation(j, i)
                                .build();
                        addButton(button);
                        continue;
                    }

                    if (button.getButtonType() == ButtonType.BACKGROUND) {
                        button.setMaterial(material);
                    }
                }
            }
        }
    }
}
