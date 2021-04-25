package me.modione.backpackplugin.main;

import me.modione.backpackplugin.utils.FileConfig;

import java.io.File;

public class unregister {
    public unregister() {
        FileConfig config = new FileConfig("settings.yml");
        config.set("enabled", register.enabled.isValue());
        config.save();
        File file = new File("plugins/BackPackPlugin/waypoints.yml");
        file.delete();
    }
}
