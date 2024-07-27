package ru.snowyk.opprison;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ConfigManager {
    static ConfigManager instance = new ConfigManager();
    FileConfiguration config;
    File configFile;
    FileConfiguration region;
    File regionFile;
    FileConfiguration bosses;
    File bossesFile;

    public static ConfigManager getInstance() {
        return instance;
    }

    public void init(Plugin plugin) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        this.configFile = new File(plugin.getDataFolder(), "config.yml");
        Exception exception;
        InputStream inputStream;
        File file;
        if (!this.configFile.exists()) {
            try {
                Bukkit.getConsoleSender().sendMessage("[OpPrison] The config.yml file cannot be found, creating one.");
                file = new File(plugin.getDataFolder(), "/config.yml");
                inputStream = this.getClass().getResourceAsStream("/config.yml");
                copyFile(inputStream, file);
            } catch (Exception var15) {
                exception = var15;
                Bukkit.getConsoleSender().sendMessage("[OpPrison] " + ChatColor.RED + "Error: The config.yml file could not be created! StackTrace:");
                exception.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        this.regionFile = new File(plugin.getDataFolder(), "region.yml");
        if (!this.regionFile.exists()) {
            try {
                Bukkit.getConsoleSender().sendMessage("[OpPrison] The region.yml file cannot be found, creating one.");
                file = new File(plugin.getDataFolder(), "/region.yml");
                inputStream = this.getClass().getResourceAsStream("/region.yml");
                copyFile(inputStream, file);
            } catch (Exception var15) {
                exception = var15;
                Bukkit.getConsoleSender().sendMessage("[OpPrison] " + ChatColor.RED + "Error: The region.yml file could not be created! StackTrace:");
                exception.printStackTrace();
            }
        }

        this.region = YamlConfiguration.loadConfiguration(this.regionFile);
        this.bossesFile = new File(plugin.getDataFolder(), "bosses.yml");
        if (!this.bossesFile.exists()) {
            try {
                Bukkit.getConsoleSender().sendMessage("[OpPrison] The region.yml file cannot be found, creating one.");
                file = new File(plugin.getDataFolder(), "/bosses.yml");
                inputStream = this.getClass().getResourceAsStream("/bosses.yml");
                copyFile(inputStream, file);
            } catch (Exception var15) {
                exception = var15;
                Bukkit.getConsoleSender().sendMessage("[OpPrison] " + ChatColor.RED + "Error: The bosses.yml file could not be created! StackTrace:");
                exception.printStackTrace();
            }
        }

        this.bosses = YamlConfiguration.loadConfiguration(this.bossesFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getRegion() {
        return region;
    }

    public FileConfiguration getBosses() {
        return bosses;
    }

    public static void copyFile(InputStream in, File out) throws Exception {
        InputStream fis = in;
        FileOutputStream fos = new FileOutputStream(out);

        try {
            byte[] buf = new byte[1024];

            int i;
            while((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } catch (Exception var9) {
            Exception e = var9;
            throw e;
        } finally {
            if (fis != null) {
                fis.close();
            }

            if (fos != null) {
                fos.close();
            }

        }

    }
}
