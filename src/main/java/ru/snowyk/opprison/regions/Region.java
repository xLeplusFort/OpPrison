package ru.snowyk.opprison.regions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import ru.snowyk.opprison.utils.V3;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class Region implements Comparable<Region> {
    private final String name;
    private final int weight;
    private final World world;
    private final V3 min;
    private final V3 max;
    private final Flags flags;

    public boolean inRegion(World world, V3 v3) {
        return world.equals(this.world) && this.min.getX() <= v3.getX() && v3.getX() <= this.max.getX() && this.min.getY() <= v3.getY() && v3.getY() <= this.max.getY() && this.min.getZ() <= v3.getZ() && v3.getZ() <= this.max.getZ();
    }

    public boolean inRegion(Player player) {
        Location location = player.getLocation();
        return inRegion(location.getWorld(), new V3(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }

    public List<Player> getPlayers() {
        return (List)Bukkit.getOnlinePlayers().stream().filter(this::inRegion).collect(Collectors.toList());
    }

    public int compareTo(Region rg) {
        return Integer.compare(rg.weight, this.weight);
    }
}
