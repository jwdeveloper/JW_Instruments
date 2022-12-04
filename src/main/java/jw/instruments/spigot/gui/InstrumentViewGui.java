package jw.instruments.spigot.gui;

import jw.fluent.api.player_context.api.PlayerContext;
import jw.fluent.api.spigot.inventory_gui.button.button_observer.ButtonObserverUI;
import jw.fluent.plugin.implementation.modules.logger.FluentLogger;
import jw.fluent.plugin.implementation.modules.translator.FluentTranslator;
import jw.instruments.core.data.PluginPermissions;
import jw.instruments.core.data.chords.Chord;
import jw.instruments.core.data.instrument.InstrumentItemStackObserver;
import jw.instruments.core.data.songs.Song;
import jw.instruments.core.factory.ButtonsFactory;
import jw.instruments.spigot.gui.songs.SongsFormGui;
import jw.instruments.spigot.gui.songs.SongsPickerGui;
import jw.instruments.core.services.ChordService;
import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Inject;
import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent.api.desing_patterns.dependecy_injection.api.enums.LifeTime;
import jw.fluent.api.spigot.inventory_gui.button.ButtonUI;
import jw.fluent.api.spigot.inventory_gui.enums.ButtonType;
import jw.fluent.api.spigot.inventory_gui.implementation.chest_ui.ChestUI;
import jw.fluent.api.utilites.java.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;



@PlayerContext
@Injection(lifeTime = LifeTime.SINGLETON)
public class InstrumentViewGui extends ChestUI {
    private final SongsFormGui songsFormGui;
    private final SongsPickerGui songsPickerGui;
    private final ChordPickerGui chordPickerGui;
    private final ChordService chordService;
    private Map<Integer, ButtonUI> chordsButtons;
    private InstrumentItemStackObserver instrumentData;
    private FluentTranslator lang;

    @Inject
    public InstrumentViewGui(ChordPickerGui chordSearchGui,
                             SongsPickerGui songsPickerGui,
                             SongsFormGui songsFormGui,
                             ChordService chordService,
                             FluentTranslator lang)
    {
        super("Instrument", 6);
        this.songsFormGui = songsFormGui;
        this.songsPickerGui = songsPickerGui;
        this.chordPickerGui = chordSearchGui;
        this.chordService = chordService;
        this.chordsButtons = new HashMap<>();
        this.lang = lang;
    }

    public void open(Player player, InstrumentItemStackObserver instrumentData) {
        this.instrumentData = instrumentData;
        open(player);
    }

    @Override
    protected void onOpen(Player player) {
        refreshChords();
    }

    @Override
    public void onInitialize() {
        chordPickerGui.setParent(this);
        songsPickerGui.setParent(this);
        songsFormGui.setParent(this);

        addPermission(PluginPermissions.INSTRUMENT_GUI);
        setTitlePrimary(lang.get("gui.instrument.title"));
        setBorderMaterial(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        chordsButtons = chordsButtons();
        for(var btn : chordsButtons.values())
        {
            this.addButton(btn);
        }
        ButtonUI.factory()
                .goBackButton(this, getParent())
                .buildAndAdd(this);
        ButtonUI.builder()
                .setMaterial(Material.WRITABLE_BOOK)
                .setPermissions(PluginPermissions.INSTRUMENT_GUI_EXPORT_SONG)
                .setTitlePrimary(lang.get("gui.instrument.export.title"))
                .setLocation(0, 6)
                .setOnClick((player, button) ->
                {
                    songsFormGui.openInsert(player, instrumentData.getChords().get());
                }).buildAndAdd(this);
        ButtonUI.builder()
                .setMaterial(Material.CHEST)
                .setPermissions(PluginPermissions.INSTRUMENT_GUI_IMPORT_SONG)
                .setTitlePrimary(lang.get("gui.instrument.import.title"))
                .setLocation(0, 7)
                .setOnClick((player, button) ->
                {
                    songsPickerGui.onContentClick((player1, button1) ->
                    {
                        var song = button1.<Song>getDataContext();
                        var chords = song.getChords();
                        instrumentData.updateChords(chords);
                        refreshChords();
                        open(player);
                    });
                    songsPickerGui.open(player);
                }).buildAndAdd(this);

        ButtonObserverUI.factory()
                .boolObserver(instrumentData.getDisplayChordsName())
                .setLocation(3,1)
                .setPermissions(PluginPermissions.INSTRUMENT_GUI_DISPLAY_CHORDS)
                .setTitlePrimary(lang.get("gui.instrument.chords.title"))
                .buildAndAdd(this);

        ButtonObserverUI.factory()
                .boolObserver(instrumentData.getChangeRhytmOnShift())
                .setLocation(3,2)
                .setPermissions(PluginPermissions.INSTRUMENT_GUI_RHYTHM_CHANGE)
                .setTitlePrimary(lang.get("gui.instrument.rhythm.title"))
                .buildAndAdd(this);

        ButtonObserverUI.factory()
                .intRangeObserver(instrumentData.getVolume(),0,100,10)
                .setLocation(3,3)
                .setPermissions(PluginPermissions.INSTRUMENT_GUI_VOLUME)
                .setTitlePrimary(lang.get("gui.instrument.volume.title"))
                .setMaterial(Material.BELL)
                .buildAndAdd(this);
    }


    private void refreshChords() {
        var chords = instrumentData.getChords().get();
        FluentLogger.LOGGER.log("Chords refresh");
        for (var i = 0; i < INVENTORY_WIDTH; i++) {
            var chordName = chords[i];
            var button = chordsButtons.get(i);
            if (StringUtils.nullOrEmpty(chordName)) {
                button.setTitlePrimary(lang.get("gui.instrument.chord-placeholder.title"));
                button.setMaterial(Material.GRAY_STAINED_GLASS_PANE);
            } else {
                var chord = chordService.find(chordName);
                button.setTitlePrimary(chord.fullName());
                button.setCustomMaterial(chord.getItemStack().getType(), chord.getCustomId());
            }
            refreshButton(button);
        }
    }

    private Map<Integer, ButtonUI> chordsButtons() {
        var result = new HashMap<Integer, ButtonUI>();
        var buttons = ButtonsFactory.createChordsButtons(lang);
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
                            instrumentData.getChords().get()[slot] = chord.fullName();
                            open(player);
                        });
                        chordPickerGui.open(player);
                    })
                    .setPermissions(PluginPermissions.INSTRUMENT_GUI_CHORDS)
                    .setOnShiftClick((player, button) ->
                    {
                        instrumentData.getChords().get()[slot] = null;
                        button.setTitlePrimary(lang.get("gui.instrument.chord-placeholder.title"));
                        button.setMaterial(Material.GRAY_STAINED_GLASS_PANE);
                        refreshButton(button);
                    })
                    .build();
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
