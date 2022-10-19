package jw.guitar.data.songs;

import jw.spigot_fluent_api.data.implementation.DataModel;
import jw.spigot_fluent_api.fluent_message.FluentMessage;
import jw.spigot_fluent_api.utilites.java.JavaUtils;
import jw.spigot_fluent_api.utilites.messages.Emoticons;
import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

@Data
public class Song extends DataModel {
    private String author = JavaUtils.EMPTY_STRING;
    private String authorUUID = JavaUtils.EMPTY_STRING;
    private String url = JavaUtils.EMPTY_STRING;

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
                .field(ChatColor.YELLOW+Emoticons.starRussin, stars.size()).newLine()
                .field("Author", author).newLine()
                .field("Chords", chords.size()).newLine();

        for (var chord : chords.entrySet()) {
            description.field("Slot: " + chord.getKey(), chord.getValue()).newLine();
        }


        description.newLine();
        description.inBrackets("temp/blockbench-info").newLine();
        description.text("Shift click to open url").newLine();
        description.text("Click to give star").newLine();

        return description.toArray();
    }
}
