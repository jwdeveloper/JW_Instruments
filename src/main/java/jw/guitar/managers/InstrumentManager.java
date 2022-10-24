package jw.guitar.managers;


import jw.fluent_api.logger.OldLogger;
import jw.guitar.chords.Chord;
import jw.guitar.gameobjects.InstrumentPlayer;
import jw.guitar.gameobjects.instuments.Instrument;
import jw.guitar.services.InstrumentDataService;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Inject;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent_api.spigot.gameobjects.implementation.GameObjectManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Injection
public class InstrumentManager
{
    private final Map<UUID, InstrumentPlayer> instrumentPlayers;
    private final InstrumentDataService instrumentDataService;

    @Inject
    public InstrumentManager(InstrumentDataService service)
    {
        this.instrumentDataService = service;
        instrumentPlayers = new HashMap<>();
    }


    public InstrumentPlayer get(Player player)
    {
        if(!validatePlayer(player))
        {
            return null;
        }

        return get(player.getUniqueId());
    }

    public InstrumentPlayer get(UUID uuid)
    {
        return instrumentPlayers.get(uuid);
    }

    public void register(Player player, ItemStack itemStack) {
        if (validatePlayer(player)) {
            unregister(player);
        }
        var data = instrumentDataService.get(itemStack);
        if (data.isEmpty()) {
            OldLogger.error("Unable to load instrument for item: " + itemStack.toString());
            return;
        }
        var instrumentData = data.get();
        final var go = InstrumentPlayer.create(player, instrumentData);

        var loc = player.getLocation().clone();
        loc.add(loc.getDirection().multiply(4));
        loc.setPitch(0);
        loc.setYaw(0);


        if (!GameObjectManager.register(go, loc)) {
            OldLogger.error("Unable to create instance of instrument: " + itemStack.toString());
            return;
        }
        instrumentPlayers.put(player.getUniqueId(), go);
        OldLogger.log("Registered", go);
    }

    public void unregister(Player player) {
        if (!validatePlayer(player)) {
            return;
        }
        final var go = instrumentPlayers.get(player.getUniqueId());
        go.onDestroy();
        GameObjectManager.unregister(go);
        instrumentPlayers.remove(player.getUniqueId());
        OldLogger.log("Unregistered", go);
    }

    public boolean validatePlayer(Player player) {
        return instrumentPlayers.containsKey(player.getUniqueId());
    }

    public boolean validateInstrument(ItemStack itemStack) {
        if (itemStack == null)
            return false;
        return Instrument.isInstrument(itemStack);
    }

    public boolean validateChord(ItemStack itemStack) {
        return Chord.isChord(itemStack);
    }
}
