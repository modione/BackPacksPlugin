package me.modione.backpackplugin.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class FileConfig extends YamlConfiguration {
    private final String path;

    public FileConfig(String name) {
        path = "plugins/BackPackPlugin/" + name;
        try {
            load(path);
        } catch (IOException | InvalidConfigurationException e) {
            System.out.println("Error in Files.");
        }
    }

    public void save() {
        try {
            save(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
