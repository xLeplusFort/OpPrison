package ru.snowyk.opprison.boss;

import me.clip.placeholderapi.PlaceholderAPI;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.snowyk.opprison.ConfigManager;
import ru.snowyk.opprison.api.OpPlayer;
import ru.snowyk.opprison.api.OpPlayerManager;
import ru.snowyk.opprison.utils.Utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class ZombieGiantBoss extends EntityGiantZombie {
    BossBar bossBar;
    FileConfiguration configuration;
    LivingEntity livingEntity;
    String name;
    int health;
    double damage;
    double followRange;
    double knobackResistence;
    double speed;
    double money;
    double tokens;
    Spawner spawner;
    Entity ent;
    HashMap<String, Integer> attackers;
    int totalDamage;
    double hpDelay;
    Random rnd;

    public ZombieGiantBoss(Spawner spawner) {
        super(EntityTypes.GIANT, ((CraftWorld)spawner.getSpawnLocation().getWorld()).getHandle());
        this.configuration = ConfigManager.getInstance().getBosses();
        this.livingEntity = (LivingEntity)this.getBukkitEntity();
        this.name = Utils.color(this.configuration.getString("mobs.giant.name")) + " §c[<3 ❤]";
        this.health = this.configuration.getInt("mobs.giant.health");
        this.damage = this.configuration.getDouble("mobs.giant.damage");
        this.followRange = this.configuration.getDouble("mobs.giant.range");
        this.knobackResistence = this.configuration.getDouble("mobs.giant.knockback-resistence");
        this.speed = this.configuration.getDouble("mobs.giant.speed");
        this.money = this.configuration.getDouble("mobs.giant.money");
        this.tokens = this.configuration.getDouble("mobs.giant.tokens");
        this.hpDelay = this.configuration.getDouble("mobs.giant.regen-delay");
        this.rnd = new Random();
        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue((double)this.health);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(this.followRange);
        this.getAttributeInstance(GenericAttributes.KNOCKBACK_RESISTANCE).setValue(this.knobackResistence);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(this.speed);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(this.damage);
        this.setHealth((float)this.health);
        ((LivingEntity)this.getBukkitEntity()).setRemoveWhenFarAway(false);
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, (float)this.followRange));
        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 10.0));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, new Class[0]));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, this.damage, true));
        (this.spawner = spawner).register(this);
        this.setCustomName(new ChatComponentText(this.name.replace("<3", String.format("%.2f", this.getHealth()))));
        this.setCustomNameVisible(true);
        this.attackers = new HashMap();
        this.totalDamage = 0;
        this.bossBar = Bukkit.createBossBar(this.name.replace("<3", String.valueOf(this.getHealth())), BarColor.RED, BarStyle.SOLID, new BarFlag[]{BarFlag.CREATE_FOG, BarFlag.DARKEN_SKY});
    }

    public boolean damageEntity(DamageSource source, float a) {
        if ((this.ent == null || source.getEntity() != this.ent) && source != DamageSource.STUCK) {
            this.setCustomName(new ChatComponentText(this.name.replace("<3", String.format("%.2f", Utils.fixDouble((double)this.getHealth())))));
            if (this.ent != null) {
                return source != DamageSource.projectile(this, this.ent) && this.ent.damageEntity(source, a);
            } else {
                Entity entity = source.j();
                Player p;
                if (entity != null && entity.getBukkitEntity().getType() == EntityType.PLAYER) {
                    p = (Player)entity.getBukkitEntity();
                    if (!this.attackers.containsKey(p.getName())) {
                        this.attackers.put(p.getName(), (int)a);
                    } else {
                        this.attackers.put(p.getName(), (int)((float)(Integer)this.attackers.get(p.getName()) + a));
                    }

                    this.totalDamage += (int)a;
                }

                org.bukkit.entity.Entity e;
                Iterator var9;
                if ((double)this.random.nextFloat() < this.configuration.getDouble("mobs.giant.effects.chance-random-potion")) {
                    var9 = this.getBukkitEntity().getNearbyEntities(10.0, 5.0, 10.0).iterator();

                    while(var9.hasNext()) {
                        e = (org.bukkit.entity.Entity)var9.next();
                        if (e.getType() == EntityType.PLAYER) {
                            PotionEffectType[] types = new PotionEffectType[]{PotionEffectType.BLINDNESS, PotionEffectType.SLOW, PotionEffectType.POISON, PotionEffectType.CONFUSION};
                            PotionEffectType effects = types[(new Random()).nextInt(types.length)];
                            ((LivingEntity)e).addPotionEffect(new PotionEffect(effects, 100, 1));
                        }
                    }
                }

                int i;
                if ((double)this.random.nextFloat() < this.configuration.getDouble("mobs.giant.effects.chance-spawn-other-mobs-zombies")) {
                    var9 = this.getBukkitEntity().getNearbyEntities(10.0, 5.0, 10.0).iterator();

                    label134:
                    while(true) {
                        do {
                            if (!var9.hasNext()) {
                                break label134;
                            }

                            e = (org.bukkit.entity.Entity)var9.next();
                        } while(!e.getType().equals(EntityType.PLAYER));

                        p = (Player)e;

                        for(i = 0; i <= this.random.nextInt(5); ++i) {
                            Zombie zombie = (Zombie)p.getWorld().spawnEntity(this.getBukkitEntity().getLocation().clone().add(0.0, 1.0, 0.0), EntityType.ZOMBIE);
                            zombie.setMaxHealth(this.configuration.getDouble("mobs.giant.health-2"));
                            zombie.setHealth(this.configuration.getDouble("mobs.giant.health-2"));
                            zombie.setCustomName(Utils.color(this.configuration.getString("mobs.giant.name-2")));
                            zombie.setTarget((LivingEntity)e);
                            zombie.setCustomNameVisible(true);
                        }

                    }
                }

                if (entity != null && !(entity instanceof EntityArrow) && !(entity instanceof EntityProjectile)) {
                    if (this.getGoalTarget() != null && this.getGoalTarget().getBukkitEntity().getVehicle() != null) {
                        this.getGoalTarget().getBukkitEntity().getVehicle().eject();
                    }

                    if (entity.getBukkitEntity() instanceof Player) {
                        p = (Player)entity.getBukkitEntity();
                        Utils.sendActionBar(p, "босса: " + Utils.color(this.configuration.getString("mobs.giant.name")), (float)Utils.fixDouble((double)this.getHealth()));
                    }
                }

                this.bossBar.setProgress((double)(this.getHealth() / (float)this.health));
                this.bossBar.setTitle(this.name.replace("<3", String.valueOf(Utils.fixDouble((double)this.getHealth()))));
                this.setCustomName(new ChatComponentText(this.name.replace("<3", String.valueOf(Utils.fixDouble((double)this.getHealth())))));
                return super.damageEntity(source, a);
            }
        } else {
            return false;
        }
    }

    public void movementTick() {
        if (this.spawner.getCurrent() != null && this.spawner.getSpawnLocation().distance(this.spawner.getCurrent().getBukkitEntity().getLocation()) > this.configuration.getDouble("mobs.giant.teleport-boss")) {
            this.spawner.getCurrent().setLocation(this.spawner.getSpawnLocation().getX(), this.spawner.getSpawnLocation().getY(), this.spawner.getSpawnLocation().getZ(), 0.0F, 0.0F);
            this.setGoalTarget((EntityLiving)null);
        }

        if (this.hpDelay-- <= 0.0) {
            this.setCustomName(new ChatComponentText(this.name.replace("<3", String.format("%.2f", this.getHealth()))));
            if (this.getHealth() < (float)this.configuration.getDouble("mobs.giant.start-health-regen")) {
                this.heal((float)this.configuration.getDouble("mobs.giant.health-regen"), RegainReason.REGEN);
            }

            this.hpDelay = 20.0 * this.configuration.getDouble("mobs.giant.regen-delay");
        }

        super.movementTick();
        if (this.spawner.getCurrent() != null) {
            Iterator var1 = this.getBukkitEntity().getNearbyEntities(32.0, 30.0, 32.0).iterator();

            while(true) {
                do {
                    while(true) {
                        org.bukkit.entity.Entity e;
                        do {
                            if (!var1.hasNext()) {
                                if (this.getGoalTarget() == null) {
                                    return;
                                }

                                if (this.getGoalTarget() != null && this.getGoalTarget().world != this.world || !(this.getGoalTarget() instanceof EntityPlayer)) {
                                    this.setGoalTarget((EntityLiving)null);
                                }

                                return;
                            }

                            e = (org.bukkit.entity.Entity)var1.next();
                        } while(e.getType() != EntityType.PLAYER);

                        Player p = (Player)e;
                        if (p.getLocation().distance(this.getBukkitEntity().getLocation()) < 15.0) {
                            break;
                        }

                        this.bossBar.removeAll();
                    }
                } while(!this.bossBar.isVisible());

                Iterator var4 = Bukkit.getOnlinePlayers().iterator();

                while(var4.hasNext()) {
                    Player players = (Player)var4.next();
                    if (players.getWorld().equals(Bukkit.getWorld("world")) && players.getLocation().distance(this.spawner.getSpawnLocation()) < 15.0) {
                        this.bossBar.addPlayer(players);
                    }
                }
            }
        }
    }

    public void die() {
        if (this.spawner != null) {
            this.spawner.dead();
        }

        if (this.killer != null) {
            this.bossBar.removeAll();
            Bukkit.broadcastMessage(Utils.color(this.configuration.getString("mobs.giant.boss-death-message").replace("%killer", this.killer.getName())));
            HashMap<String, Double> percents = Utils.calculatePercents(this.attackers, (double)this.totalDamage);
            Iterator var2 = percents.keySet().iterator();

            while(var2.hasNext()) {
                String key = (String)var2.next();
                double money = (Double)percents.get(key) * this.money;
                double tokens = Utils.fixDouble((Double)percents.get(key) * this.tokens);
                Player player = Bukkit.getPlayer(key);
                OpPlayer opPlayer = OpPlayerManager.getOpPlayer(player.getName());
                if (player != null) {

                    if (money < 0.0) {
                        money = 0.0;
                    }

                    if (tokens < 0.0) {
                        tokens = 0.0;
                    }

                    try {
                        player.sendMessage(Utils.color("&r"));
                        player.sendMessage(Utils.color(this.configuration.getString("settings.boss-death").replace("%money", Utils.getString(money))));
                        opPlayer.setMoney(opPlayer.getMoney() + money);

                        opPlayer.setTokens(opPlayer.getTokens() + tokens);
                        player.sendMessage(Utils.color(this.configuration.getString("settings.boss-tokens-death").replace("%tokens", Utils.getString(tokens))));

                        player.sendMessage(Utils.color("&r"));
                    } catch (NullPointerException var11) {
                        NullPointerException ex = var11;
                        ex.printStackTrace();
                    }
                }
            }
        }

        super.die();
    }
}
