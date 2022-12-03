package jw.instruments.spigot.gui.songs;

import jw.fluent_api.player_context.api.PlayerContext;
import jw.fluent_plugin.implementation.modules.translator.FluentTranslator;
import jw.instruments.core.data.songs.Song;
import jw.instruments.core.data.songs.SongsRepository;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Inject;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent_api.desing_patterns.dependecy_injection.api.enums.LifeTime;
import jw.fluent_api.spigot.inventory_gui.implementation.list_ui.ListUI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
@PlayerContext
@Injection(lifeTime = LifeTime.TRANSIENT)
public class SongsPickerGui extends ListUI<Song> {
    private final SongsRepository repository;
    private final FluentTranslator lang;

    @Inject
    public SongsPickerGui(SongsRepository songsRepository,
                          FluentTranslator lang) {
        super("pick song", 6);
        this.repository = songsRepository;
        this.lang = lang;
    }


    @Override
    protected void onInitialize() {
        setListTitlePrimary(lang.get("gui.song.picker.title"));
        setContentButtons(repository.findAll(), (song, button) ->
        {
            button.setMaterial(song.getIcon());
            button.setTitlePrimary(song.getName());
            button.setDescription(song.getDescriptionLines());
            button.setDataContext(song);
            button.setOnClick((player, button1) ->
            {
                var _song = button1.<Song>getDataContext();
                _song.giveStar(player);
                refreshContent();
            });
        });

        onListOpen(player ->
        {
            refreshContent();
        });

    }
}
