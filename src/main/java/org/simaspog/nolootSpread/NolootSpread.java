package org.simaspog.nolootSpread;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public final class NolootSpread extends JavaPlugin implements Listener {
    private final Random random = new Random();
    private final Map<Player, ItemStack[]> pendingDrops = new HashMap<>();
    private int taskId;
    private boolean enableSpread;
    private double spreadRadius;
    private double dropVelocity;
    private int checkInterval;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.loadConfigValues();
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getCommand("nolootspread").setExecutor(new ReloadCommand(this));
        this.getLogger().info("NoLootSpread enabled - Spread radius: " + this.spreadRadius + " blocks");

        long intervalTicks = (long) this.checkInterval * 20L;
        this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Map.Entry<Player, ItemStack[]> entry : new HashMap<>(this.pendingDrops).entrySet()) {
                Player player = entry.getKey();
                if (!player.isOnline() || Boolean.TRUE.equals(player.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY))) {
                    continue;
                }
                this.dropPendingItems(player, entry.getValue());
                this.pendingDrops.remove(player);
            }
        }, 0L, intervalTicks);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(this.taskId);
        this.pendingDrops.forEach(this::dropPendingItems);
        this.pendingDrops.clear();
        this.getLogger().info("NoLootSpread disabled");
    }

    private void loadConfigValues() {
        this.enableSpread = this.getConfig().getBoolean("enable-spread", true);
        this.spreadRadius = this.getConfig().getDouble("spread-radius", 1.0);
        this.dropVelocity = this.getConfig().getDouble("drop-velocity", 0.1);
        this.checkInterval = this.getConfig().getInt("check-interval", 30);
        if (this.spreadRadius < 0.0) {
            this.getLogger().warning("spread-radius cannot be negative! Using default value of 1.0");
            this.spreadRadius = 1.0;
        }
        if (this.checkInterval < 1) {
            this.getLogger().warning("check-interval cannot be less than 1! Using default value of 30");
            this.checkInterval = 30;
        }
    }

    public void reloadPluginConfig() {
        this.reloadConfig();
        this.loadConfigValues();
        this.getLogger().info("Configuration reloaded - Spread radius: " + this.spreadRadius + " blocks");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location deathLocation = player.getLocation().add(0.0, 0.5, 0.0);
        boolean keepInventory = event.getKeepInventory() || Boolean.TRUE.equals(player.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY));
        if (!keepInventory) {
            this.processDeathDrops(event, player, deathLocation);
        } else if (Boolean.TRUE.equals(player.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY)) && !event.getKeepInventory()) {
            this.storeInventoryForLater(player, event);
        }
    }

    private void storeInventoryForLater(Player player, PlayerDeathEvent event) {
        if (event.getKeepInventory()) {
            return;
        }
        ItemStack[] contents = player.getInventory().getContents().clone();
        ItemStack[] armor = player.getInventory().getArmorContents().clone();
        ItemStack offhand = player.getInventory().getItemInOffHand().clone();
        ItemStack[] allItems = new ItemStack[contents.length + armor.length + 1];
        System.arraycopy(contents, 0, allItems, 0, contents.length);
        System.arraycopy(armor, 0, allItems, contents.length, armor.length);
        allItems[allItems.length - 1] = offhand;
        this.pendingDrops.put(player, allItems);
        event.setKeepInventory(true);
        event.getDrops().clear();
        player.getInventory().clear();
    }

    private void processDeathDrops(PlayerDeathEvent event, Player player, Location deathLocation) {
        event.setKeepInventory(false);
        event.getDrops().clear();
        ItemStack offhandItem = player.getInventory().getItemInOffHand();
        if (offhandItem != null && !offhandItem.getType().isAir()) {
            this.dropItemWithSpread(offhandItem.clone(), deathLocation, player);
            player.getInventory().setItemInOffHand(null);
        }
        ItemStack[] armor = player.getInventory().getArmorContents();
        for (int i = 0; i < armor.length; ++i) {
            if (armor[i] == null || armor[i].getType().isAir()) continue;
            this.dropItemWithSpread(armor[i].clone(), deathLocation, player);
            armor[i] = null;
        }
        player.getInventory().setArmorContents(armor);
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType().isAir()) continue;
            this.dropItemWithSpread(item.clone(), deathLocation, player);
        }
        player.getInventory().clear();
    }

    private void dropPendingItems(Player player, ItemStack[] items) {
        Location dropLocation = player.getLocation().add(0.0, 0.5, 0.0);
        for (ItemStack item : items) {
            if (item == null || item.getType().isAir()) continue;
            this.dropItemWithSpread(item, dropLocation, player);
        }
    }

    private void dropItemWithSpread(ItemStack item, Location center, Player player) {
        Location dropLoc;
        if (this.enableSpread && this.spreadRadius > 0.0) {
            double angle = this.random.nextDouble() * Math.PI * 2.0;
            double distance = this.random.nextDouble() * this.spreadRadius;
            double offsetX = Math.cos(angle) * distance;
            double offsetZ = Math.sin(angle) * distance;
            dropLoc = center.clone().add(offsetX, 0.0, offsetZ);
        } else {
            dropLoc = center.clone();
        }
        Item dropped = player.getWorld().dropItem(dropLoc, item);
        dropped.setVelocity(new Vector(0.0, this.dropVelocity, 0.0));
    }
}
