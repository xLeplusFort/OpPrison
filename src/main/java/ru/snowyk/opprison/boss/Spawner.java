package ru.snowyk.opprison.boss;

import net.minecraft.server.v1_16_R3.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import ru.snowyk.opprison.ConfigManager;
import ru.snowyk.opprison.utils.Utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Spawner {
    public static Map<UUID, Spawner> spawners = new HashMap();
    Location location;
    CustomEntity type;
    Entity current;
    long deathTime;
    int interval;
    long deathTimeG;
    private final UUID uuid;
    private ArmorStand am;

    public Spawner(Location location, CustomEntity type, int interval) {
        this.location = location;
        this.current = null;
        this.deathTime = -1L;
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.interval = interval;
        spawners.put(this.uuid, this);
        (this.am = (ArmorStand)location.getWorld().spawnEntity(location.clone().add(ConfigManager.getInstance().getBosses().getDouble("settings.hologram-x"), ConfigManager.getInstance().getBosses().getDouble("settings.hologram-y"), ConfigManager.getInstance().getBosses().getDouble("settings.hologram-z")), EntityType.ARMOR_STAND)).setCustomName("§aБосс Пробудился");
        this.am.setCustomNameVisible(true);
        this.am.setVisible(false);
        this.am.setGravity(false);
        this.am.setSmall(true);
    }

    public Spawner(Location location, CustomEntity type) {
        this.location = location;
        this.current = null;
        this.deathTime = -1L;
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.interval = -1;
        spawners.put(this.uuid, this);
        (this.am = (ArmorStand)location.getWorld().spawnEntity(location.clone().add(ConfigManager.getInstance().getBosses().getDouble("settings.hologram-x"), ConfigManager.getInstance().getBosses().getDouble("settings.hologram-y"), ConfigManager.getInstance().getBosses().getDouble("settings.hologram-z")), EntityType.ARMOR_STAND)).setCustomName("§aБосс Пробудился");
        this.am.setCustomNameVisible(true);
        this.am.setVisible(false);
        this.am.setGravity(false);
        this.am.setSmall(true);
    }

    public void spawn() {
        if (this.location != null && this.location.getChunk() != null && this.location.getChunk().isLoaded() && this.current == null) {
            CustomEntity.spawnEntity(this.type, this.location.clone().add(0.0, 1.0, 0.0), this);
        }

        CustomEntity[] var1 = CustomEntity.values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            CustomEntity type = var1[var3];
            if (this.type.equals(type)) {
                Bukkit.broadcastMessage("§f ");
                Bukkit.broadcastMessage(Utils.color(ConfigManager.getInstance().getBosses().getString("broadcast.boss-respawn-" + this.type.toString().toLowerCase() + "-message")));
                Bukkit.broadcastMessage("§f ");
            }
        }

    }

    private String calculateTime(long seconds) {
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.SECONDS.toHours(seconds) * 60L;
        long second = TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.SECONDS.toMinutes(seconds) * 60L;
        long hours = TimeUnit.SECONDS.toHours(seconds);
        return "§e" + hours + " час. " + minute + " мин. " + second + " сек.";
    }

    public void dead() {
        this.current = null;
        this.deathTime = System.currentTimeMillis() / 1000L;
        this.deathTimeG = 0L;
        this.am.setCustomName(this.calculateTime((long)this.interval - this.deathTimeG));
    }

    public Location getSpawnLocation() {
        return this.location;
    }

    public void update() {
        if (this.current == null) {
            if (System.currentTimeMillis() / 1000L - this.deathTime >= (long)this.interval && this.interval > 0) {
                this.spawn();
                this.am.setCustomName(Utils.color(ConfigManager.getInstance().getBosses().getString("settings.hologram-boss-spawned")));
            } else {
                ++this.deathTimeG;
                this.am.setCustomName(this.calculateTime((long)this.interval - this.deathTimeG));
            }
        } else if (this.current.getBukkitEntity().getLocation().distance(this.location) > 64.0) {
            this.current.getBukkitEntity().teleport(this.location);
        }

    }

    public void register(Entity entity) {
        this.current = entity;
    }

    public void removeHolo() {
        this.am.remove();
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public Entity getCurrent() {
        return this.current;
    }

    public CustomEntity getType() {
        return this.type;
    }

    public static class SpawnerUpdater extends BukkitRunnable {
        public SpawnerUpdater() {
        }

        public void run() {
            Iterator var1 = Spawner.spawners.values().iterator();

            while(var1.hasNext()) {
                Spawner cSpawner = (Spawner)var1.next();
                cSpawner.update();
            }

        }
    }

    public static class SpawnerUtils extends Spawner {
        public SpawnerUtils(Location location, CustomEntity type, int interval) {
            super(location, type, interval);
        }

        public static void init() {
            FileConfiguration bosses = ConfigManager.getInstance().getBosses();
            Iterator var1 = bosses.getConfigurationSection("mobs").getKeys(false).iterator();

            while(var1.hasNext()) {
                String currentSpawn = (String)var1.next();
                ConfigurationSection path = bosses.getConfigurationSection("mobs").getConfigurationSection(currentSpawn);
                Location loc = null;

                try {
                    String[] locFull = path.getString("location").split(" ");
                    loc = new Location(Bukkit.getWorld(locFull[0]), Double.parseDouble(locFull[1]), Double.parseDouble(locFull[2]), Double.parseDouble(locFull[3]));
                } catch (NumberFormatException | NullPointerException var7) {
                    RuntimeException ex13 = var7;
                    ex13.printStackTrace();
                }

                CustomEntity type = (CustomEntity)Enum.valueOf(CustomEntity.class, path.getString("type").toUpperCase());
                int interval = path.getInt("interval");
                (new Spawner(loc, type, interval)).update();
            }

        }
    }
}
