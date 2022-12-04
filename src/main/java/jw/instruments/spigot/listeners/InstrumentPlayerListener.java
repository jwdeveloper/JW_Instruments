package jw.instruments.spigot.listeners;

import jw.instruments.core.managers.InstrumentManager;
import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Inject;
import jw.fluent.api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent.api.spigot.events.EventBase;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.*;
import org.bukkit.event.server.PluginEnableEvent;


@Injection(lazyLoad = false)
public class InstrumentPlayerListener extends EventBase {
    private final InstrumentManager manager;

    @Inject
    public InstrumentPlayerListener(InstrumentManager instrumentManager) {
        this.manager = instrumentManager;
    }

    @EventHandler
    public void onChangeSlotEvent(PlayerSwapHandItemsEvent event) {

        var player = event.getPlayer();
        var itemInOffHand = player.getInventory().getItemInOffHand();
        var itemInMainHand = player.getInventory().getItemInMainHand();
        if (manager.validateInstrument(itemInOffHand)) {

            manager.unregister(player);
            return;
        }

        if (manager.validateInstrument(itemInMainHand)) {
            manager.register(player, itemInMainHand);
        }
    }

    @EventHandler
    public void onSlotChange(PlayerItemHeldEvent event)
    {
        var player = event.getPlayer();
        var itemInOffHand = player.getInventory().getItemInOffHand();
        if (!manager.validateInstrument(itemInOffHand)) {
            return;
        }
        var instrumentPlayer = manager.get(player.getUniqueId());
        if(instrumentPlayer == null)
        {
            return;
        }
        instrumentPlayer.changeChord(event.getNewSlot());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        var player = event.getPlayer();
        var action = event.getAction();
        var isLeftClick = switch (action) {
            case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK -> false;
            default -> true;
        };
        if (!manager.validatePlayer(player)) {
            manager.tryRegister(player);
            return;
        }
        var itemInOffHand = player.getInventory().getItemInOffHand();
        if (!manager.validateInstrument(itemInOffHand)) {
            manager.unregister(player);
            return;
        }
        event.setCancelled(true);
        manager.get(player.getUniqueId()).playChord(isLeftClick);
    }

    @EventHandler
    public void changePlayingStyle(PlayerToggleSneakEvent event) {
        final var player = event.getPlayer();
        if (!manager.validatePlayer(player)) {
            return;
        }
        if (!player.isSneaking()) {
            return;
        }
        manager.get(player.getUniqueId()).changeStyle(player);
    }
    @Override
    public void onPluginStart(PluginEnableEvent event) {
        for (var player : Bukkit.getOnlinePlayers()) {
            final var offHandItem = player.getInventory().getItemInOffHand();
            if (!manager.validateInstrument(offHandItem)) {
                return;
            }
            manager.register(player, offHandItem);
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        final var player = playerJoinEvent.getPlayer();
        if (manager.validatePlayer(player)) {
            return;
        }
        final var offHandItem = player.getInventory().getItemInOffHand();
        if (!manager.validateInstrument(offHandItem)) {
            return;
        }
        manager.register(player, offHandItem);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        manager.unregister(event.getPlayer());
    }

}
