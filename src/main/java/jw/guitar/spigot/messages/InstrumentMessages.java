package jw.guitar.spigot.messages;

import jw.guitar.core.rhythms.Rhythm;
import jw.fluent_api.spigot.messages.FluentMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class InstrumentMessages
{
    public static void rhythmChange(Player player, Rhythm rhythm)
    {
        FluentMessage.message()
                .inBrackets("Guitar style", ChatColor.AQUA)
                .color(ChatColor.GRAY).space()
                .text(rhythm.getName())
                .send(player);
    }
}
