package ru.snowyk.opprison.boss;

import net.minecraft.server.v1_16_R3.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public enum CustomEntity {
    GIANT("§c§lЗомби Гигант", 53, EntityType.GIANT, ZombieGiantBoss.class);

    private String name;
    private int id;
    private EntityType entityType;
    private Class<? extends Entity> customClass;

    private CustomEntity(String name, int id, EntityType entityType, Class customClass) {
        this.name = name;
        this.id = id;
        this.entityType = entityType;
        this.customClass = customClass;
    }

    public static void spawnEntity(CustomEntity entityType, Location location, Spawner spawner) {
        try {
            Entity entity = (Entity)entityType.getCustomClass().getConstructor(Spawner.class).newInstance(spawner);
            entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            ((CraftWorld)location.getWorld()).getHandle().addEntity(entity, SpawnReason.CUSTOM);
        } catch (Exception var4) {
            Exception ex = var4;
            ex.printStackTrace();
        }

    }

    public String getName() {
        return this.name;
    }

    public int getID() {
        return this.id;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public Class<? extends Entity> getCustomClass() {
        return this.customClass;
    }
}
