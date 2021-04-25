package me.modione.backpackplugin.utils;

import me.modione.backpackplugin.utils.Setting;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class SettingsCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.hasPermission("bp.settings")) return false;
        Player player = (Player) sender;
        player.openInventory(Setting.settingsinv);
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
