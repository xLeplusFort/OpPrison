package ru.snowyk.opprison;

import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.snowyk.opprison.api.OpPlayer;
import ru.snowyk.opprison.api.OpPlayerManager;
import ru.snowyk.opprison.boss.Spawner;
import ru.snowyk.opprison.database.MySQL;
import ru.snowyk.opprison.regions.RegionConfig;
import ru.snowyk.opprison.regions.RegionManager;
import ru.snowyk.opprison.regions.WorldGuard;
import ru.snowyk.opprison.scoreboard.ScoreBoard;
import ru.snowyk.opprison.tools.pickaxe.PickaxeListener;
import ru.snowyk.opprison.utils.Placeholders;
import ru.snowyk.opprison.utils.Utils;

import java.util.Iterator;
import java.util.Map;

public final class OpPrison extends JavaPlugin {
    public static OpPrison instance;
    public static ConfigManager configManager = ConfigManager.getInstance();
    private MySQL mySQL;
    public RegionManager regionManager;

    @Override
    public void onEnable() {
        instance = this;
        configManager.init(this);
        this.regionManager = new RegionManager();
        RegionConfig.load();
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            this.refreshStatistic();
        }, 600L, (long)this.getConfig().getDouble("MySQL.refresh-statistic") * 20L);
        (new Spawner.SpawnerUpdater()).runTaskTimer(this, 0L, 20L);
        Spawner.SpawnerUtils.init();
        this.connectMySQL();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            (new Placeholders(this)).register();
        }
        Bukkit.getPluginManager().registerEvents(new OpPlayerManager(), this);
        Bukkit.getPluginManager().registerEvents(new ScoreBoard(), this);
        Bukkit.getPluginManager().registerEvents(new PickaxeListener(), this);
        Bukkit.getPluginManager().registerEvents(new WorldGuard(regionManager), this);
        Bukkit.getPluginManager().registerEvents(new GlobalListener(), this);
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            Iterator var0 = ScoreBoard.getBoards().values().iterator();

            while(var0.hasNext()) {
                BPlayerBoard board = (BPlayerBoard)var0.next();
                ScoreBoard.updateScoreBoard(board);
            }

        }, 0L, 20L);
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        Spawner.spawners.values().forEach(Spawner::removeHolo);
        this.refreshStatistic();
        disconnectMySQL();
        // Plugin shutdown logic
    }

    public void connectMySQL() {
        String database = configManager.getConfig().getString("MySQL.database");
        String address = configManager.getConfig().getString("MySQL.address");
        String user = configManager.getConfig().getString("MySQL.user");
        String password = configManager.getConfig().getString("MySQL.password");
        this.mySQL = new MySQL(address, database, user, password);
    }

    public void disconnectMySQL() {
        this.mySQL.close();
    }

    private void refreshStatistic() {
        Iterator var1 = OpPlayerManager.manager.entrySet().iterator();

        while (var1.hasNext()) {
            Map.Entry<String, OpPlayer> entry = (Map.Entry) var1.next();
            if (entry != null) {
                OpPlayer customPlayer = (OpPlayer) OpPlayerManager.manager.get(entry.getKey());
                MySQL.setDoulblecolumn((String) entry.getKey(), "blocks", Utils.fixDouble(customPlayer.getBlocks()));
                MySQL.setDoulblecolumn((String) entry.getKey(), "money", Utils.fixDouble(customPlayer.getMoney()));
                MySQL.setDoulblecolumn((String) entry.getKey(), "multiplier", Utils.fixDouble(customPlayer.getMultiplier()));
                MySQL.setDoulblecolumn((String) entry.getKey(), "prestige", Utils.fixDouble(customPlayer.getPrestige()));
                MySQL.setDoulblecolumn((String) entry.getKey(), "tokens", Utils.fixDouble(customPlayer.getTokens()));
            }
        }
    }
}
