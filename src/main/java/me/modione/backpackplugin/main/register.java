package me.modione.backpackplugin.main;

import me.modione.backpackplugin.utils.FileConfig;
import me.modione.backpackplugin.utils.ItemBuilder;
import me.modione.backpackplugin.utils.Setting;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public class register {
    public static Setting enabled;
    public register() {
        Setting.register();
        // Enabled Option
        FileConfig config = new FileConfig("settings.yml");
        try {
            if (config.get("enabled")!=null) {
                enabled = new Setting(new ItemBuilder(Material.ITEM_FRAME).displayname(ChatColor.LIGHT_PURPLE+"Enabled").build(), config.getBoolean("enabled"));
                BackPackPlugin.enabled = enabled.isValue();
            }else {
                enabled = new Setting(new ItemBuilder(Material.ITEM_FRAME).displayname(ChatColor.LIGHT_PURPLE+"Enabled").build(), true);
            }
        }catch (Exception ignored) {}
        ItemStack oi = enabled.getItem();
        enabled.setLorefalse(Arrays.asList(ChatColor.AQUA + "Toggles wheter crafting backpacks, executing commands and open backpacks works.",
                ChatColor.AQUA + "The plugin is currently " + ChatColor.RED + "disabled"));
        enabled.setLoretrue(Arrays.asList(ChatColor.AQUA + "Toggles wheter crafting backpacks, executing commands and open backpacks works.",
                ChatColor.AQUA + "The plugin is currently " + ChatColor.GREEN + "enabled"));
        enabled.setOn_Value_change(() -> BackPackPlugin.enabled = enabled.isValue());
        enabled.refresh(oi);
    }
}
