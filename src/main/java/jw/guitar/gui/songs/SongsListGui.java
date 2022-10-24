package jw.guitar.gui.songs;

import jw.fluent_api.logger.OldLogger;
import jw.guitar.data.PluginPermissions;
import jw.guitar.data.songs.Song;
import jw.guitar.data.songs.SongsRepository;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Inject;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent_api.desing_patterns.dependecy_injection.api.enums.LifeTime;
import jw.fluent_api.spigot.gui.implementation.crud_list_ui.CrudListUI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;


@Injection(lifeTime = LifeTime.TRANSIENT)
public class SongsListGui extends CrudListUI<Song> {

    private SongsRepository repository;
    private SongsFormGui songsInsertGui;

    @Inject
    public SongsListGui(SongsRepository chordSetRepository,
                        SongsFormGui songsInsertGui
    ) {
        super("songs", 6);
        this.repository = chordSetRepository;
        this.songsInsertGui = songsInsertGui;
        songsInsertGui.setParent(this);
    }



    @Override
    protected void onInitialize() {
        setupPermissions();
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
            button.setOnShiftClick((player, button1) ->
            {

                var _song = button1.<Song>getDataContext();
                var msg = new TextComponent("click to open url");
                msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, _song.getUrl()));
                close();
                player.spigot().sendMessage(msg);
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
                setTitle(Lang.get("gui.base.delete.error"));
            }
            refreshContent();
        });
        onListOpen(player ->
        {
            OldLogger.log("Repo", repository.findAll().size() + " ");
            refreshContent();
        });

        addSearchStrategy("By title",event ->
        {
            return event.data().getName().contains(event.searchKey());
        });

        addSearchStrategy("By author",event ->
        {
            var res = event.data().getAuthor().contains(event.searchKey());;
            OldLogger.log("Search",event.data().getAuthor(), event.searchKey(), res);
            return res;
        });
    }


    private void setupPermissions() {
        getButtonInsert().setPermissions(PluginPermissions.SONGS_INSERT);
        getButtonEdit().setPermissions(PluginPermissions.SONGS_EDIT);
        getButtonDelete().setPermissions(PluginPermissions.SONGS_DELETE);
    }
}
