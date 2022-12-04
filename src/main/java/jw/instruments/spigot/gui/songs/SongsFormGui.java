package jw.instruments.spigot.gui.songs;

import jw.fluent_api.player_context.api.PlayerContext;
import jw.fluent_plugin.implementation.modules.translator.FluentTranslator;
import jw.instruments.core.data.chords.Chord;
import jw.instruments.core.data.songs.Song;
import jw.instruments.core.data.songs.SongsRepository;
import jw.instruments.core.factory.ButtonsFactory;
import jw.instruments.spigot.gui.ChordPickerGui;
import jw.instruments.core.services.ChordService;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Inject;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent_api.desing_patterns.dependecy_injection.api.enums.LifeTime;
import jw.fluent_api.spigot.inventory_gui.EventsListenerInventoryUI;
import jw.fluent_api.spigot.inventory_gui.button.ButtonUI;
import jw.fluent_api.spigot.inventory_gui.implementation.chest_ui.ChestUI;
import jw.fluent_api.spigot.inventory_gui.implementation.items_list_ui.ItemsSearchUI;
import jw.fluent_api.spigot.messages.FluentMessage;
import jw.fluent_api.utilites.java.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@PlayerContext
@Injection(lifeTime = LifeTime.TRANSIENT)
public class SongsFormGui extends ChestUI {

    private Song song;
    private SongsRepository songsRepository;
    private ChordPickerGui chordSearchGui;
    private ItemsSearchUI itemsListUI;
    private ChordService chordService;
    private boolean isInsert = false;
    private Map<Integer, ButtonUI> chordsButtons;
    private FluentTranslator lang;

    @Inject
    public SongsFormGui(SongsRepository songsRepository,
                        ChordPickerGui chordSearchGui,
                        ChordService chordService,
                        FluentTranslator translator) {
        super("insert", 6);
        this.songsRepository = songsRepository;
        this.chordSearchGui = chordSearchGui;
        this.chordService = chordService;
        this.itemsListUI = new ItemsSearchUI();
        this.lang = translator;
    }


    public void openInsert(Player player, String[] chords) {

        song = new Song();
        song.setName(player.getName() + "'s song");
        song.setAuthor(player.getName());
        song.setAuthorUUID(player.getUniqueId().toString());
        song.setIcon(Material.MUSIC_DISC_WARD);

        var chord = new HashMap<Integer,String>();
        for(var i =0;i<chords.length;i++)
        {
            if(chords[i] == null)
                continue;
            chord.put(i,chords[i]);
        }
        song.setChords(chord);
        isInsert = true;

        open(player);
    }

    public void openInsert(Player player) {
        openInsert(player,new String[0]);
    }

    public void openEdit(Player player, Song song) {
        var copyOptional = song.<Song>copy();
        if (copyOptional.isEmpty()) {
            openParent();
            return;
        }
        this.song = copyOptional.get();
        isInsert = false;
        open(player);
    }

    @Override
    protected void onOpen(Player player) {
        refreshChords();
    }

    @Override
    protected void onInitialize() {

        setFillMaterial(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        setTitlePrimary(lang.get("gui.song.form.title"));
        chordsButtons = createChordButtons();
        savesButtons();
        itemsListUI.setParent(this);
        chordSearchGui.setParent(this);
        ButtonUI.builder()
                .setMaterial(Material.PAPER)
                .setTitlePrimary(lang.get("gui.base.title"))
                .setLocation(3, 2)
                .setDescription(song.getName())
                .setOnClick((player, button) ->
                {
                    close();
                    FluentMessage.message()
                            .info().text(lang.get("gui.song.form.message"))
                            .send(player);

                    EventsListenerInventoryUI.registerTextInput(player, s ->
                    {
                        song.setName(s);
                        button.setDescription(s);
                        open(player);
                    });
                })
                .buildAndAdd(this);

        ButtonUI.builder()
                .setMaterial(Material.MUSIC_DISC_13)
                .setTitlePrimary(lang.get("gui.base.icon"))
                .setLocation(3, 4)
                .setOnClick((player, button) ->
                {
                    itemsListUI.onContentClick((player1, button1) ->
                    {
                        var material = button1.<Material>getDataContext();
                        button.setMaterial(material);
                        song.setIcon(material);
                        open(player);
                    });
                    itemsListUI.open(player);
                })
                .buildAndAdd(this);
    }

    public Map<Integer,ButtonUI> createChordButtons() {
        var result = new HashMap<Integer, ButtonUI>();
        var chords = song.getChords();
        var buttons = ButtonsFactory.createChordsButtons(lang);
        for (var i = 0; i < INVENTORY_WIDTH; i++) {
            final var slot = i;
            final var builder = buttons.get(i);
            final var btn = builder.setOnClick((player, button) ->
                    {
                        chordSearchGui.onContentClickDynamic((p, b) ->
                        {
                            var chord = b.<Chord>getDataContext();
                            button.setTitlePrimary(chord.fullName());
                            button.setCustomMaterial(chord.getItemStack().getType(), chord.getCustomId());
                            chords.put(slot, chord.fullName());
                            open(player);
                        });
                        chordSearchGui.open(player);
                    })
                    .setOnShiftClick((player, button) ->
                    {
                        if (!chords.containsKey(slot)) {
                            return;
                        }
                        chords.remove(slot);
                        button.setTitlePrimary(lang.get("gui.instrument.chord-placeholder.title"));
                        button.setMaterial(Material.GRAY_STAINED_GLASS_PANE);
                        refreshButton(button);
                    })
                    .buildAndAdd(this);
            result.put(slot,btn);
        }
        return result;
    }

    private void refreshChords()
    {
        var chords = song.getChords();
        for(var i =0;i<INVENTORY_WIDTH;i++)
        {
            var chordName = chords.getOrDefault(i, StringUtils.EMPTY_STRING);
            var button = chordsButtons.get(i);
            if(chordName.equals(StringUtils.EMPTY_STRING))
            {
                button.setTitlePrimary(lang.get("gui.instrument.chord-placeholder.title"));
                button.setMaterial(Material.GRAY_STAINED_GLASS_PANE);
            }
            else
            {
                var chord = chordService.find(chordName);
                button.setTitlePrimary(chord.fullName());
                button.setCustomMaterial(chord.getItemStack().getType(), chord.getCustomId());
            }
        }
    }

    public void savesButtons() {
        ButtonUI.builder()
                .setMaterial(Material.LIME_STAINED_GLASS_PANE)
                .setLocation(5, 3)
                .setTitlePrimary(lang.get("gui.base.save"))
                .setOnClick((player, button) ->
                {
                    if (isInsert) {
                        var result = songsRepository.insert(song);
                    } else {
                        var result = songsRepository.update(song.getUuid(), song);
                    }
                    openParent();
                })
                .buildAndAdd(this);

        ButtonUI.builder()
                .setMaterial(Material.RED_STAINED_GLASS_PANE)
                .setLocation(5, 5)
                .setTitlePrimary(lang.get("gui.base.cancel"))
                .setOnClick((player, button) ->
                {
                    openParent();
                })
                .buildAndAdd(this);
    }
}
