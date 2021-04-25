package me.modione.backpackplugin.utils;

import me.modione.backpackplugin.main.BackPackPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import static me.modione.backpackplugin.main.BackPackPlugin.backpackAinv;

public class Listener implements org.bukkit.event.Listener {
    private final Random random = new Random();

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (backpackAinv.containsKey(event.getItemInHand())) event.setCancelled(true);
    }

    @EventHandler
    public void onInvMove(InventoryMoveItemEvent event) {
        if (backpackAinv.containsValue(event.getDestination()) || backpackAinv.containsValue(event.getSource()) && backpackAinv.containsKey(event.getItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (backpackAinv.containsValue(event.getInventory()) && backpackAinv.containsKey(event.getCurrentItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onRename(PrepareAnvilEvent event) {
        try {
            AnvilInventory inventory = event.getInventory();
            if (backpackAinv.containsKey(inventory.getItem(0)) || backpackAinv.containsKey(inventory.getItem(1))) {
                event.setResult(null);
            }
        } catch (NullPointerException ignored) {
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (backpackAinv.containsKey(event.getItem())) {
            if (!BackPackPlugin.enabled) {
                event.getPlayer().sendMessage(ChatColor.RED+"Backpacks are currently disabled!");
                return;
            }
            event.setCancelled(true);
            event.getPlayer().openInventory(backpackAinv.get(event.getItem()));
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (event.getAction().equals(InventoryAction.NOTHING) || event.getAction().equals(InventoryAction.UNKNOWN)) {
            event.setCancelled(true);
            return;
        }
        if (BackPackPlugin.stack.equals(event.getRecipe().getResult())) {
            if (!BackPackPlugin.enabled) {
                event.getWhoClicked().sendMessage(ChatColor.RED+"Backpacks are currently disabled!");
                event.setCancelled(true);
                return;
            }
            if (Objects.requireNonNull(event.getInventory().getItem(0)).getAmount() > 1 &&
                    event.getAction().name().contains("ALL")) {
                event.setCancelled(true);
                return;
            }
            ItemStack stack = event.getRecipe().getResult();
            ItemMeta meta = stack.getItemMeta();
            assert meta != null;
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < 11; i++) {
                builder.append("§").append(random.nextInt(8) + 1).append(i);
            }
            meta.setLore(Arrays.asList("§" + (random.nextInt(8) + 1) + "You can store items in here.", String.valueOf(builder)));
            stack.setItemMeta(meta);
            for (ItemStack itemStack : backpackAinv.keySet()) {
                while (Objects.equals(Objects.requireNonNull(itemStack.getItemMeta()).getLore(), meta.getLore())) {
                    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder") StringBuilder builder1 = new StringBuilder();
                    for (int i = 1; i < 11; i++) {
                        builder1.append("§").append(random.nextInt(8) + 1).append(i);
                    }
                    meta.setLore(Arrays.asList("§" + (random.nextInt(8) + 1) + "You can store items in here.", String.valueOf(builder)));
                    stack.setItemMeta(meta);
                }
            }
            event.setCurrentItem(stack);
            backpackAinv.put(stack, Bukkit.createInventory(null, 36, "BackPack"));
        }
    }
}
