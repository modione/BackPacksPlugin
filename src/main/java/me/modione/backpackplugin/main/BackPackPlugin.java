package me.modione.backpackplugin.main;

import me.modione.backpackplugin.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings({"ALL", "unchecked", "ResultOfMethodCallIgnored"})
public final class BackPackPlugin extends JavaPlugin {
    @SuppressWarnings("SpellCheckingInspection")
    public static final HashMap<ItemStack, Inventory> backpackAinv = new HashMap<>();
    public static final HashMap<UUID, ItemStack> arm = new HashMap<>();
    public static Recipe recipe;
    public static ItemStack stack;
    public static boolean enabled = true;
    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public void onEnable() {
        // Plugin startup logic
        new register();
        Bukkit.getPluginManager().registerEvents(new Listener(), this);
        Bukkit.getPluginManager().registerEvents(new SettingListener(), this);
        Bukkit.getPluginCommand("bpsettings").setExecutor(new SettingsCommand());
        stack = new ItemBuilder(Material.CHEST)
                .displayname(ChatColor.AQUA + "BACKPACK")
                .build();
        recipe = recipe(stack);
        Bukkit.addRecipe(recipe);
        try {
            FileConfig config = new FileConfig("backpacks.yml");
            List<String> UUIDS = (List<String>) config.getList("UUID");
            List<ItemStack> ITEMSTACKS = (List<ItemStack>) config.getList("ITEMSTACK");
            for (String s : Objects.requireNonNull(UUIDS)) {
                FileConfig config1 = new FileConfig("backpacks/" + s + ".yml");
                List<ItemStack> contents = (List<ItemStack>) config1.getList("Inventory");
                Inventory inventory = Bukkit.createInventory(null, 36, "BackPack");
                assert ITEMSTACKS != null;
                backpackAinv.put(ITEMSTACKS.get(UUIDS.indexOf(s)), inventory);
                AtomicInteger slot = new AtomicInteger();
                assert contents != null;
                for (ItemStack content : contents) {
                    if (content == null) {
                        slot.getAndIncrement();
                        continue;
                    }
                    inventory.setItem(slot.get(), content);
                    slot.getAndIncrement();
                }
            }
            File file1 = new File("plugins/BackPackPlugin/backpacks");
            for (File s : Objects.requireNonNull(file1.listFiles())) {
                if (!UUIDS.contains(s.getName().replace(".yml", ""))) s.delete();
            }
        } catch (Exception ignored) {
            File file1;
            try {
                file1 = new File("plugins/BackPackPlugin");
                for (File s : Objects.requireNonNull(file1.listFiles())) {
                    s.delete();
                }
            } catch (Exception exception) {
                System.out.println("First Time using BackPackPlugin?");
            }
            System.out.println("BackPack files not found!");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        new unregister();
        try {
            if (backpackAinv.isEmpty()) return;
            FileConfig config = new FileConfig("backpacks.yml");
            @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") HashMap<UUID, Inventory> as = new HashMap<>();
            for (ItemStack itemStack : backpackAinv.keySet()) {
                if (!itemStack.getType().equals(Material.CHEST)) continue;
                UUID id = UUID.randomUUID();
                arm.put(id, itemStack);
                as.put(id, backpackAinv.get(itemStack));
            }
            List<String> saves = new ArrayList<>();
            for (UUID uuid : arm.keySet()) {
                saves.add(uuid.toString());
            }
            config.set("UUID", saves);
            List<ItemStack> saves1 = new ArrayList<>(arm.values());
            config.set("ITEMSTACK", saves1);
            config.save();
            File file = new File("plugins/BackPackPlugin/backpacks/");
            file.mkdirs();
            for (UUID uuid : arm.keySet()) {
                FileConfig fileConfig = new FileConfig("backpacks/" + uuid.toString() + ".yml");
                List<ItemStack> list = Arrays.asList((backpackAinv.get(arm.get(uuid)).getContents()));
                fileConfig.set("Inventory", list);
                fileConfig.save();
            }
        } catch (Exception ignored) {
        }
    }
    public static ShapedRecipe recipe(ItemStack itemStack) {
        ShapedRecipe recipe = new ShapedRecipe(itemStack);
        recipe.shape("LLL", "LDL", "LLL");
        recipe.setIngredient('L', Material.LEATHER);
        recipe.setIngredient('D', Material.DIAMOND);
        recipe.setGroup("BackPacks");
        return recipe;
    }
}
