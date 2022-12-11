package jw.instruments.spigot.messages;

import jw.instruments.core.rhythms.Rhythm;
import jw.fluent.plugin.implementation.modules.messages.FluentMessage;
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
