package jw.instruments.spigot.gameobjects.factory;

import jw.instruments.core.data.instument.InstrumentDataObserver;
import jw.instruments.spigot.gameobjects.InstrumentPlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class InstrumentPlayerFactory
{
    public static InstrumentPlayer create(Player player, InstrumentDataObserver instrumentData) {
        final var go = new InstrumentPlayer();
        go.setPlayer(player);
        go.setData(instrumentData);
        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC,1,1);
        return go;
    }
}
