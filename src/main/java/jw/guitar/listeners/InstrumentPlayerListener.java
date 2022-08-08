package jw.guitar.listeners;

import jw.guitar.gameobjects.InstrumentPlayer;
import jw.guitar.gameobjects.instuments.Instrument;
import jw.guitar.services.InstrumentDataService;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.annotations.Inject;
import jw.spigot_fluent_api.desing_patterns.dependecy_injection.annotations.Injection;
import jw.spigot_fluent_api.fluent_events.EventBase;
import jw.spigot_fluent_api.fluent_gameobjects.implementation.GameObjectManager;
import jw.spigot_fluent_api.fluent_logger.FluentLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Injection(lazyLoad = false)
public class InstrumentPlayerListener extends EventBase {


    private final Map<UUID, InstrumentPlayer> instrumentPlayers;
    private final InstrumentDataService dataService;

    @Inject
    public InstrumentPlayerListener(InstrumentDataService service) {
        instrumentPlayers = new HashMap<>();
        this.dataService = service;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        var player = event.getPlayer();
        if (!validatePlayer(player)) {
            return;
        }
        var itemInOffHand = player.getInventory().getItemInOffHand();
        if (!validateInstrument(itemInOffHand)) {
            return;
        }
        var go = instrumentPlayers.get(player.getUniqueId());
        go.playChord(event);
        go.handlePlayerInteraction(player,player.getEyeLocation(),10);
    }


    @EventHandler
    public void onChangeSlotEvent(PlayerSwapHandItemsEvent event) {

        var player = event.getPlayer();
        var itemInOffHand = player.getInventory().getItemInOffHand();
        var itemInMainHand = player.getInventory().getItemInMainHand();

        //FluentLogger.warning("off",itemInOffHand.getItemMeta().getDisplayName());
        //  FluentLogger.warning("main",itemInMainHand.getItemMeta().getDisplayName());
        //  FluentLogger.success("triggered",event.toString(),itemInMainHand.getItemMeta().getDisplayName());
        if (validateInstrument(itemInOffHand)) {
            FluentLogger.log("valid main hand", event.toString());
            unregister(player);
            return;
        }

        if (validateInstrument(itemInMainHand)) {
            FluentLogger.log("register", event.toString());
            register(player, itemInMainHand);
        }

    }


    @EventHandler
    public void onPlayerAttacked(EntityDamageByEntityEvent e) {

        if (!(e.getDamager().getType() == EntityType.PLAYER)) {
            return;
        }
        final var player = (Player) e.getDamager();
        if (!validatePlayer(player)) {
            return;
        }
        final var itemStack = player.getInventory().getItemInMainHand();
        if (!validateInstrument(itemStack)) {
            return;
        }
        instrumentPlayers.get(player.getUniqueId()).makeNoise();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        var player = (Player) e.getWhoClicked();
        if (!validatePlayer(player)) {
            return;
        }
        var item = e.getCurrentItem();
        if (!validateInstrument(item)) {
            return;
        }
        if (!e.getAction().equals(InventoryAction.PLACE_ALL)) {
            return;
        }
        e.setCancelled(true);

        final var go = instrumentPlayers.get(player.getUniqueId());
        go.openGUI();
    }

    @EventHandler
    public void changePlayingStyle(PlayerToggleSneakEvent event) {
        final var player = event.getPlayer();
        if (!validatePlayer(player)) {
            return;
        }
        if (!player.isSneaking()) {
            return;
        }
        var go = instrumentPlayers.get(player.getUniqueId());
        if (go == null)
            return;
        go.changeStyle();
    }


    @Override
    public void onPluginStart(PluginEnableEvent event) {
        for (var player : Bukkit.getOnlinePlayers()) {
            final var offHandItem = player.getInventory().getItemInOffHand();
            if (!validateInstrument(offHandItem)) {
                return;
            }
            register(player, offHandItem);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        final var player = playerJoinEvent.getPlayer();
        if (validatePlayer(player)) {
            return;
        }
        final var offHandItem = player.getInventory().getItemInOffHand();
        if (!validateInstrument(offHandItem)) {
            return;
        }
        register(player, offHandItem);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        unregister(event.getPlayer());
    }

    private void register(Player player, ItemStack itemStack) {
        if (validatePlayer(player)) {
            unregister(player);
        }
        var data = dataService.get(itemStack);
        if (data.isEmpty()) {
            FluentLogger.error("Unable to load instrument for item: " + itemStack.toString());
            return;
        }
        var instrumentData = data.get();
        final var go = InstrumentPlayer.create(player, instrumentData);


        var loc = player.getLocation().clone();
        loc.add(loc.getDirection().multiply(4));
        loc.setPitch(0);
        loc.setYaw(0);
        if (!GameObjectManager.register(go, loc)) {
            FluentLogger.error("Unable to create instance of instrument: " + itemStack.toString());
            return;
        }
        instrumentPlayers.put(player.getUniqueId(), go);
        FluentLogger.log("Registered", go);
    }

    public void unregister(Player player) {
        if (!validatePlayer(player)) {
            return;
        }
        final var go = instrumentPlayers.get(player.getUniqueId());
        GameObjectManager.unregister(go);
        instrumentPlayers.remove(player.getUniqueId());
        FluentLogger.log("Unregistered", go);
    }


    private boolean validatePlayer(Player player) {
        return instrumentPlayers.containsKey(player.getUniqueId());
    }

    private boolean validateInstrument(ItemStack itemStack) {
        if (itemStack == null)
            return false;
        return Instrument.isInstrument(itemStack);
    }
}
