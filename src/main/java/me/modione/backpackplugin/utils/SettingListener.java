package me.modione.backpackplugin.utils;

import me.modione.backpackplugin.utils.Setting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class SettingListener implements Listener {
    @EventHandler
    public void on_Click(InventoryClickEvent event) {
        if (!Objects.equals(event.getClickedInventory(), Setting.settingsinv)) return;
        for (Setting setting : Setting.settings) {
            if (setting.getItem().equals(event.getCurrentItem())) {
                event.setCancelled(true);
                if (setting.isValue()) {
                    setting.setValue(!setting.isValue());
                    setting.getOn_Value_change().run();
                    ItemMeta meta = setting.getItem().getItemMeta();
                    Objects.requireNonNull(meta).setLore(setting.getLoretrue());
                    ItemStack oi = setting.getItem();
                    setting.getItem().setItemMeta(meta);
                    setting.refresh(oi);
                } else {
                    setting.setValue(!setting.isValue());
                    setting.getOn_Value_change().run();
                    ItemMeta meta = setting.getItem().getItemMeta();
                    Objects.requireNonNull(meta).setLore(setting.getLorefalse());
                    ItemStack oi = setting.getItem();
                    setting.getItem().setItemMeta(meta);
                    setting.refresh(oi);
                }
            }
        }
    }
}
