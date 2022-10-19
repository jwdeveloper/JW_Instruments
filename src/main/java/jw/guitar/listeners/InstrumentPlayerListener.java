package jw.guitar.listeners;

import jw.guitar.managers.InstrumentManager;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Inject;
import jw.fluent_api.desing_patterns.dependecy_injection.api.annotations.Injection;
import jw.fluent_api.minecraft.events.EventBase;
import jw.fluent_api.minecraft.logger.FluentLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
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
        manager.get(player.getUniqueId()).changeChord(event.getNewSlot());
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
            if (isLeftClick) {
                return;
            }

            var mainHand = player.getInventory().getItemInMainHand();
            if (!manager.validateInstrument(mainHand)) {
                return;
            }
            player.getInventory().setItemInOffHand(mainHand);
            player.getInventory().setItemInMainHand(null);
            manager.register(player, mainHand);
            return;
        }
        var itemInOffHand = player.getInventory().getItemInOffHand();
        if (!manager.validateInstrument(itemInOffHand)) {
            return;
        }
        event.setCancelled(true);
        manager.get(player.getUniqueId()).playChord(isLeftClick);
    }


    @EventHandler
    public void onEvent(InventoryEvent event)
    {
        FluentLogger.log(event.toString()+" GOOD EVENZ");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        var player = (Player) e.getWhoClicked();
        var item = e.getCurrentItem();
        if (!manager.validateChord(item)) {
            return;
        }
        player.setItemOnCursor(null);
        e.setCurrentItem(null);
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
        manager.get(player.getUniqueId()).changeStyle();
    }


    @EventHandler
    public void onPlayerAttacked(EntityDamageByEntityEvent e) {

        if (!(e.getDamager().getType() == EntityType.PLAYER)) {
            return;
        }
        final var player = (Player) e.getDamager();
        if (!manager.validatePlayer(player)) {
            return;
        }
        final var itemStack = player.getInventory().getItemInMainHand();
        if (!manager.validateInstrument(itemStack)) {
            return;
        }
        manager.get(player.getUniqueId()).makeNoise();
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
    public void onItemDrop(PlayerDropItemEvent e) {
        final var player = (Player) e.getPlayer();
        if (!manager.validatePlayer(player)) {
            return;
        }
        e.setCancelled(true);
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
