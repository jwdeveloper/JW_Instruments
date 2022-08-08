package jw.guitar.gui.songs;

import jw.guitar.data.songs.Song;
import jw.guitar.data.songs.SongsRepository;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.annotations.Inject;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.annotations.Injection;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.enums.LifeTime;
import jw.spigot_fluent_api.fluent_gui.implementation.list_ui.ListUI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

@Injection(lifeTime = LifeTime.TRANSIENT)
public class SongsPickerGui extends ListUI<Song> {
    private final SongsRepository repository;

    @Inject
    public SongsPickerGui(SongsRepository songsRepository) {
        super("pick song", 6);
        this.repository = songsRepository;
    }


    @Override
    protected void onInitialize() {
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
    }
}
