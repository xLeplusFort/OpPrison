package ru.snowyk.opprison.regions;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import ru.snowyk.opprison.ConfigManager;
import ru.snowyk.opprison.mines.Mine;
import ru.snowyk.opprison.mines.ResourceBlock;
import ru.snowyk.opprison.utils.V3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RegionConfig {
    protected static RegionManager regionManager;

    // немног не очень, но ладно

    public static void load() {
        List<Region> listRegion = new ArrayList();
        FileConfiguration configuration = ConfigManager.getInstance().getRegion();
        if (configuration.getConfigurationSection("regions") != null) {
            configuration.getConfigurationSection("regions").getKeys(false).forEach((key) -> {
                ConfigurationSection path = configuration.getConfigurationSection("regions." + key);
                ConfigurationSection location = configuration.getConfigurationSection("regions." + key + ".location");
                ConfigurationSection flags = configuration.getConfigurationSection("regions." + key + ".flags");
                World world = Bukkit.getWorld(location.getString("world"));
                String[] min = location.getString("min").split(" ");
                int minX = Integer.parseInt(min[0]);
                int minY = Integer.parseInt(min[1]);
                int minZ = Integer.parseInt(min[2]);
                String[] max = location.getString("max").split(" ");
                int maxX = Integer.parseInt(max[0]);
                int maxY = Integer.parseInt(max[1]);
                int maxZ = Integer.parseInt(max[2]);
                boolean blockbreak = false;
                boolean blockplace = false;
                boolean pvp = false;
                if (flags.contains("blockbreak")) {
                    blockbreak = flags.getBoolean("blockbreak");
                }
                if (flags.contains("blockplace")) {
                    blockplace = flags.getBoolean("blockplace");
                }
                if (flags.contains("pvp")) {
                    pvp = flags.getBoolean("pvp");
                }
                listRegion.add(new Region(key.toLowerCase(), 1, world, new V3(minX, minY, minZ), new V3(maxX, maxY, maxZ), new Flags(blockbreak, blockplace, pvp)));
            });
        }
        listRegion.add(new Global(0, new Flags(false, false, false)));
        regionManager.clearRegions();
        regionManager.addRegions(listRegion);
        ArrayList<Mine> mines = new ArrayList();
        if (configuration.getConfigurationSection("mines") != null) {
            configuration.getConfigurationSection("mines").getKeys(false).forEach((key) -> {
                ConfigurationSection mine = configuration.getConfigurationSection("mines." + key);
                int update = mine.getInt("update");
                List<Region> mineRegions = mine.getStringList("regions").stream().map(regionManager::byId).filter(Objects::nonNull).collect(Collectors.toList());
                List<ResourceBlock> content = ResourceBlock.fromList(mine.getStringList("content"));
                ResourceBlock upperContent = ResourceBlock.fromString(mine.getString("upperContent"));
                if (update != 0) {
                    mines.add(Mine.builder().id(key.toLowerCase()).regions(mineRegions).content(content).upperContent(upperContent).update(update).build());
                }
            });
        }
        regionManager.getMineManager().clearMines();
        regionManager.getMineManager().addMines(mines);
    }
}
