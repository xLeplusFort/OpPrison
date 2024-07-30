package ru.snowyk.opprison.regions;

import io.netty.util.internal.ConcurrentSet;
import lombok.Getter;
import org.bukkit.Location;
import ru.snowyk.opprison.mines.MineManager;
import ru.snowyk.opprison.utils.V3;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class RegionManager {
    private final WorldGuard worldGuard;
    private final RegionConfig regionConfig;
    private final MineManager mineManager;
    private final Set<Region> regions = new ConcurrentSet();

    public RegionManager() {
        this.worldGuard = new WorldGuard(this);
        this.mineManager = new MineManager();
        RegionConfig.regionManager = this;
        this.regionConfig = new RegionConfig();
    }

    public void clearRegions() {
        this.regions.clear();
    }

    public void addRegions(Region... regions) {
        this.regions.addAll(Arrays.asList(regions));
    }

    public void addRegions(List<Region> regions) {
        this.regions.addAll(regions);
    }

    public Region byId(String id) {
        Iterator var2 = this.regions.iterator();

        Region rg;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            rg = (Region)var2.next();
        } while(!rg.getName().equals(id));

        return rg;
    }

    public List<Region> getContainRegions(Location loc) {
        V3 point = new V3(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        return (List)this.regions.stream().filter((r) -> {
            return r.inRegion(loc.getWorld(), point);
        }).sorted().collect(Collectors.toList());
    }

    public Region getPrimaryRegion(Location loc) {
        return (Region)this.getContainRegions(loc).get(0);
    }
}
