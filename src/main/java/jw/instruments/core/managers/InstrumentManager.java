package jw.instruments.core.managers;


import jw.fluent.plugin.implementation.FluentApi;
import jw.fluent.plugin.implementation.modules.player_context.implementation.FluentPlayerContext;
import jw.instruments.core.services.InstrumentItemStackFactory;
import jw.instruments.spigot.gameobjects.InstrumentPlayer;
import jw.instruments.core.instuments.Instrument;
import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Inject;
import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent.api.spigot.gameobjects.implementation.GameObjectManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Injection
public class InstrumentManager {
    private final Map<UUID, InstrumentPlayer> instrumentPlayers;
    private final InstrumentItemStackFactory instrumentDataService;
    private final FluentPlayerContext playerContext;

    @Inject
    public InstrumentManager(InstrumentItemStackFactory service, FluentPlayerContext playerContext) {
        this.instrumentDataService = service;
        this.playerContext = playerContext;
        instrumentPlayers = new HashMap<>();
    }


    public InstrumentPlayer get(Player player) {
        if (!validatePlayer(player)) {
            return null;
        }

        return get(player.getUniqueId());
    }

    public InstrumentPlayer get(UUID uuid) {
        return instrumentPlayers.get(uuid);
    }



    public void register(Player player, ItemStack itemStack) {
        if (validatePlayer(player)) {
            unregister(player);
        }
        var optional = instrumentDataService.create(itemStack);
        if (optional.isEmpty()) {
            FluentApi.logger().error("Unable to load instrument for item: " + itemStack.toString());
            return;
        }
        var instrumentData = optional.get();
        var gameObject = playerContext.find(InstrumentPlayer.class, player);
        gameObject.setInstrument(instrumentData);
        if (!GameObjectManager.register(gameObject, player.getLocation())) {
            FluentApi.logger().error("Unable to create instance of instrument: " + itemStack.toString());
            return;
        }
        instrumentPlayers.put(player.getUniqueId(), gameObject);
    }

    public void unregister(Player player) {
        if (!validatePlayer(player)) {
            return;
        }
        final var go = instrumentPlayers.get(player.getUniqueId());
        go.onDestroy();
        GameObjectManager.unregister(go);
        instrumentPlayers.remove(player.getUniqueId());
    }

    public boolean validatePlayer(Player player) {
        return instrumentPlayers.containsKey(player.getUniqueId());
    }

    public boolean validateInstrument(ItemStack itemStack) {
        if (itemStack == null)
            return false;
        return Instrument.isInstrument(itemStack);
    }


}
