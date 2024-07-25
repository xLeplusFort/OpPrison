package ru.snowyk.opprison.regions;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import ru.snowyk.opprison.utils.V3;

import java.util.List;

public class Global extends Region {
    public Global(int weight, Flags flags) {
        super("__global__", weight, (World)null, (V3)null, (V3)null, flags);
    }

    public List<Player> getPlayers() {
        return (List)Bukkit.getOnlinePlayers();
    }

    public boolean inRegion(World world, V3 v3) {
        return true;
    }

    public boolean inRegion(Player player) {
        return true;
    }
}
