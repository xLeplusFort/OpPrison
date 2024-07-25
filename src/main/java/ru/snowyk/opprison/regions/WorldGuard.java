package ru.snowyk.opprison.regions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class WorldGuard implements Listener {
    private RegionManager regionManager;

    public WorldGuard(RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @EventHandler
    private void onBreak(BlockBreakEvent event) {
        Region region = this.regionManager.getPrimaryRegion(event.getBlock().getLocation());
        event.setCancelled(!region.getFlags().isBlockbreak());
    }

    @EventHandler
    private void onBreak(BlockPlaceEvent event) {
        Region region = this.regionManager.getPrimaryRegion(event.getBlock().getLocation());
        event.setCancelled(!region.getFlags().isBlockplace());
    }

    @EventHandler
    private void onPvp(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Region region = this.regionManager.getPrimaryRegion(event.getEntity().getLocation());
            Region region1 = this.regionManager.getPrimaryRegion(event.getDamager().getLocation());
            event.setCancelled(!region.getFlags().isPvp() || !region1.getFlags().isPvp());
        }
    }
}
