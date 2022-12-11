package jw.instruments.core.data.songs;

import jw.fluent.api.files.api.models.DataModel;
import jw.fluent.api.spigot.inventory_gui.InventoryUI;
import jw.fluent.plugin.implementation.modules.messages.FluentMessage;
import jw.fluent.api.utilites.java.StringUtils;
import jw.fluent.api.utilites.messages.Emoticons;
import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

@Data
public class Song extends DataModel {
    private String author = StringUtils.EMPTY_STRING;
    private String authorUUID = StringUtils.EMPTY_STRING;
    private String url = StringUtils.EMPTY_STRING;

    private Map<Integer, String> chords = new HashMap<>();
    private Set<String> stars = new HashSet<>();


    public void giveStar(Player player)
    {
        var uuid = player.getUniqueId().toString();
        if(stars.contains(uuid))
        {
            stars.remove(uuid);
        }
        else
        {
            stars.add(uuid);
        }
    }

    public String[] getDescriptionLines() {
        var description = FluentMessage
                .message()
                .field("Author", author).newLine()
                .color(ChatColor.GRAY)
                .bar(Emoticons.line,20)
                .newLine();

        for(var i =0;i< InventoryUI.INVENTORY_WIDTH;i++)
        {
            var text= chords.get(i);
            text = text == null? Emoticons.no : ChatColor.AQUA+text;
            description.field(String.valueOf(i+1),text).newLine();
        }

        return description.toArray();
    }
}
