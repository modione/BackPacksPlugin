package me.modione.backpackplugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Setting {
    public static Inventory settingsinv = Bukkit.createInventory(null, 36, "Settings");
    public static List<Setting> settings = new ArrayList<>();
    private ItemStack item;
    private Runnable on_Value_change;
    private List<String> loretrue;
    private List<String> lorefalse;
    private boolean value;

    public Setting(ItemStack item) {
        this.item = item.clone();
        settings.add(this);
        refresh();
    }

    public Setting(ItemStack item, Runnable on_Value_change) {
        this(item);
        this.on_Value_change = on_Value_change;
    }

    public Setting(ItemStack item, Runnable on_Value_change, Boolean defaultvalue) {
        this(item, on_Value_change);
        setValue(defaultvalue);
    }

    public Setting(ItemStack item, Boolean defaultvalue) {
        this(item);
        setValue(defaultvalue);
    }

    public static void register() {
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(ItemStack stack) {
        ItemStack oi = stack.clone();
        this.item = stack;
        refresh(oi);
    }

    public void refresh(ItemStack olditem) {
        if (!settingsinv.contains(olditem)) return;
        ItemMeta meta = getItem().getItemMeta();
        assert meta != null;
        if (isValue()) {
            meta.setLore(getLoretrue());
        } else {
            meta.setLore(getLorefalse());
        }
        getItem().setItemMeta(meta);
        int slot = settingsinv.first(olditem.getType());
        if (slot==-1) {
            slot=settingsinv.firstEmpty();
            System.out.println("THE FUCKING SHIT IS BROKEN");
        }
        settingsinv.remove(olditem);
        settingsinv.setItem(slot, getItem());
    }

    public void refresh() {
        ItemMeta meta = getItem().getItemMeta();
        assert meta != null;
        if (isValue()) {
            meta.setLore(getLoretrue());
        } else {
            meta.setLore(getLorefalse());
        }
        getItem().setItemMeta(meta);
        settingsinv.setItem(settingsinv.firstEmpty(), getItem());
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public Runnable getOn_Value_change() {
        return on_Value_change;
    }

    public void setOn_Value_change(Runnable on_Value_change) {
        this.on_Value_change = on_Value_change;
    }

    public List<String> getLorefalse() {
        return lorefalse;
    }

    public void setLorefalse(List<String> lorefalse) {
        this.lorefalse = lorefalse;
    }

    public List<String> getLoretrue() {
        return loretrue;
    }

    public void setLoretrue(List<String> loretrue) {
        this.loretrue = loretrue;
    }
}