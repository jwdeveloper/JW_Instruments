package jw.instruments.spigot.gui.songs;

import jw.fluent.api.player_context.api.PlayerContext;
import jw.fluent.plugin.implementation.modules.translator.FluentTranslator;
import jw.instruments.core.data.PluginPermissions;
import jw.instruments.core.data.songs.Song;
import jw.instruments.core.data.songs.SongsRepository;
import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Inject;
import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent.api.desing_patterns.dependecy_injection.api.enums.LifeTime;
import jw.fluent.api.spigot.inventory_gui.implementation.crud_list_ui.CrudListUI;


@PlayerContext
@Injection(lifeTime = LifeTime.SINGLETON)
public class SongsListGui extends CrudListUI<Song> {

    private SongsRepository repository;
    private SongsFormGui songsInsertGui;
    private FluentTranslator lang;

    @Inject
    public SongsListGui(SongsRepository chordSetRepository,
                        SongsFormGui songsInsertGui,
                        FluentTranslator lang
    ) {
        super("songs", 6);
        this.repository = chordSetRepository;
        this.songsInsertGui = songsInsertGui;
        this.lang = lang;
        songsInsertGui.setParent(this);
    }

    @Override
    protected void onInitialize() {
        setupPermissions();
        addPermission(PluginPermissions.SONGS_GUI);
        setListTitlePrimary(lang.get("gui.song.list.title"));
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
        onInsert((player, button) ->
        {
            songsInsertGui.openInsert(player);
        });

        onEdit((player, button) -> {
            var song = button.<Song>getDataContext();
            songsInsertGui.openEdit(player, song);
        });
        onDelete((player, button) ->
        {
            var song = button.<Song>getDataContext();
            var result = repository.deleteOne(song);
            if (!result) {
                setTitle(lang.get("gui.base.delete.error"));
            }
            refreshContent();
        });
        onListOpen(player ->
        {
            refreshContent();
        });

        addSearchStrategy(lang.get("gui.song.list.search.by-title"), event -> event.data().getName().contains(event.searchKey()));
        addSearchStrategy(lang.get("gui.song.list.search.by-author"), event -> event.data().getAuthor().contains(event.searchKey()));
    }


    private void setupPermissions() {
        getButtonInsert().setPermissions(PluginPermissions.SONGS_INSERT);
        getButtonEdit().setPermissions(PluginPermissions.SONGS_EDIT);
        getButtonDelete().setPermissions(PluginPermissions.SONGS_DELETE);
    }
}
