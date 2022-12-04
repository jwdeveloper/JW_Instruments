package jw.instruments.spigot.gui.songs;

import jw.fluent.api.player_context.api.PlayerContext;
import jw.fluent.plugin.implementation.modules.translator.FluentTranslator;
import jw.instruments.core.data.songs.Song;
import jw.instruments.core.data.songs.SongsRepository;
import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Inject;
import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent.api.desing_patterns.dependecy_injection.api.enums.LifeTime;
import jw.fluent.api.spigot.inventory_gui.implementation.list_ui.ListUI;

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
