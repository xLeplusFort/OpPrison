package ru.snowyk.opprison;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class GlobalListener implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent event) {
        FileConfiguration config = ConfigManager.getInstance().getConfig();
        ConfigurationSection location = config.getConfigurationSection("spawn");
        event.setJoinMessage((String)null);
        event.getPlayer().teleport(new Location(Bukkit.getWorld(location.getString("world")), location.getDouble("x"), location.getDouble("y"), location.getDouble("z")));
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        event.setQuitMessage((String)null);
    }

    @EventHandler
    public void death(PlayerDeathEvent event) {
        event.setDeathMessage((String)null);
        event.setKeepInventory(true);
        event.getDrops().clear();
    }

    @EventHandler
    public void respawn(PlayerRespawnEvent e) {
        FileConfiguration config = ConfigManager.getInstance().getConfig();
        ConfigurationSection location = config.getConfigurationSection("spawn");
        e.setRespawnLocation(new Location(Bukkit.getWorld(location.getString("world")), location.getDouble("x"), location.getDouble("y"), location.getDouble("z")));
    }
}
