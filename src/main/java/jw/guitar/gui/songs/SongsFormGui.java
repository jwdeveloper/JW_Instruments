package jw.guitar.gui.songs;

import jw.guitar.chords.Chord;
import jw.guitar.data.songs.Song;
import jw.guitar.data.songs.SongsRepository;
import jw.guitar.factory.ButtonsFactory;
import jw.guitar.gui.ChordPickerGui;
import jw.guitar.services.ChordService;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.annotations.Inject;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.annotations.Injection;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.enums.LifeTime;
import jw.spigot_fluent_api.fluent_gui.EventsListenerInventoryUI;
import jw.spigot_fluent_api.fluent_gui.button.ButtonUI;
import jw.spigot_fluent_api.fluent_gui.implementation.chest_ui.ChestUI;
import jw.spigot_fluent_api.fluent_gui.implementation.items_list_ui.ItemsSearchUI;
import jw.spigot_fluent_api.fluent_logger.FluentLogger;
import jw.spigot_fluent_api.fluent_message.FluentMessage;
import jw.spigot_fluent_api.fluent_plugin.languages.Lang;
import jw.spigot_fluent_api.utilites.java.JavaUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;


@Injection(lifeTime = LifeTime.TRANSIENT)
public class SongsFormGui extends ChestUI {

    private Song song;
    private SongsRepository songsRepository;
    private ChordPickerGui chordSearchGui;
    private ItemsSearchUI itemsListUI;
    private ChordService chordService;


    private boolean isInsert = false;
    private Map<Integer, ButtonUI> chordsButtons;

    @Inject
    public SongsFormGui(SongsRepository songsRepository,
                        ChordPickerGui chordSearchGui,
                        ChordService chordService) {
        super("insert", 6);
        this.songsRepository = songsRepository;
        this.chordSearchGui = chordSearchGui;
        this.chordService = chordService;
        this.itemsListUI = new ItemsSearchUI();
        setEnableLogs(true);
    }


    public void openInsert(Player player, Map<Integer, String> chords) {
        song = new Song();
        song.setName(player.getName() + "'s song");
        song.setAuthor(player.getName());
        song.setAuthorUUID(player.getUniqueId().toString());
        song.setIcon(Material.MUSIC_DISC_WARD);
        song.getChords().putAll(chords);
        isInsert = true;

        open(player);
    }

    public void openInsert(Player player) {
        openInsert(player,new HashMap<>());
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
        chordsButtons = createChordButtons();
        savesButtons();
        itemsListUI.setParent(this);
        chordSearchGui.setParent(this);
        ButtonUI.builder()
                .setMaterial(Material.PAPER)
                .setTitlePrimary("Rename")
                .setLocation(3, 6)
                .setDescription(Lang.get("gui.piano.rename.desc"))
                .setOnClick((player, button) ->
                {
                    close();
                    FluentMessage.message()
                            .color(org.bukkit.ChatColor.AQUA)
                            .bold()
                            .inBrackets("Piano info")
                            .space().
                            reset().
                            text("Write new piano's name on the chat").send(player);
                    EventsListenerInventoryUI.registerTextInput(player, s ->
                    {
                        song.setName(s);
                        setTitle(s);
                        open(player);
                    });
                })
                .buildAndAdd(this);

        ButtonUI.builder()
                .setMaterial(Material.PAPER)
                .setTitlePrimary("song url")
                .setLocation(3, 2)
                .setDescription("add music url")
                .setOnClick((player, button) ->
                {
                    close();
                    EventsListenerInventoryUI.registerTextInput(player, s ->
                    {
                        song.setUrl(s);
                        open(player);
                    });
                })
                .buildAndAdd(this);

        ButtonUI.builder()
                .setMaterial(Material.DIRT)
                .setTitlePrimary("Icon")
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
        var buttons = ButtonsFactory.createChordsButtons();
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
                            chords.putIfAbsent(slot, chord.fullName());
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
                        button.setTitlePrimary("Chord place holder");
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

    public void savesButtons() {
        ButtonUI.builder()
                .setMaterial(Material.LIME_STAINED_GLASS_PANE)
                .setLocation(5, 3)
                .setTitlePrimary("Save")
                .setOnClick((player, button) ->
                {
                    if (isInsert) {
                        var result = songsRepository.insert(song);
                        FluentLogger.log("inserting", result);
                    } else {
                        var result = songsRepository.update(song.getUuid(), song);
                        FluentLogger.log("updating", result);
                    }
                    openParent();
                })

                .buildAndAdd(this);

        ButtonUI.builder()
                .setMaterial(Material.RED_STAINED_GLASS_PANE)
                .setLocation(5, 5)
                .setTitlePrimary("Cancel")
                .setOnClick((player, button) ->
                {
                    openParent();
                })
                .buildAndAdd(this);
    }
}
