package ru.snowyk.opprison.api;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.snowyk.opprison.database.MySQL;
import ru.snowyk.opprison.tools.pickaxe.Pickaxe;
import ru.snowyk.opprison.utils.JsonUtil;
import ru.snowyk.opprison.utils.Utils;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class OpPlayerManager implements Listener {
    public static HashMap<String, OpPlayer> manager = new HashMap();

    public static OpPlayer getOpPlayer(String name) {
        return manager.containsKey(name) ? (OpPlayer) manager.get(name) : null;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws IOException {
        Player player = event.getPlayer();
        if (!this.opPlayerExists(player.getName())) {
            this.createOpPlayer(player);
            manager.put(player.getName(), new OpPlayer(Ranks.A, new Pickaxe(player), 0.0, 0.0, 0.0, 0.0, 0.0));
        } else {
            Ranks rank = Ranks.fromOrdinal(MySQL.getIntegercolumn(player.getName(), "rank"));
            Pickaxe pickaxe = (Pickaxe)JsonUtil.fromJson(MySQL.getStringcolumn(player.getName(), "pickaxe"), Pickaxe.class);
            double blocks = MySQL.getDounlecolumn(player.getName(), "blocks");
            double money = MySQL.getDounlecolumn(player.getName(), "money");
            double multiplier = MySQL.getDounlecolumn(player.getName(), "multiplier");
            double prestige = MySQL.getDounlecolumn(player.getName(), "prestige");
            double tokens = MySQL.getDounlecolumn(player.getName(), "tokens");
            manager.put(player.getName(), new OpPlayer(rank, pickaxe, blocks, money, multiplier, prestige, tokens));
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) throws IOException {
        Player player = event.getPlayer();
        String name = player.getName();
        OpPlayer opPlayer = getOpPlayer(name);
        if (manager.containsKey(name) && opPlayer != null) {
            MySQL.setIntegercolumn(name, "rank", opPlayer.getRank().ordinal());
            MySQL.setStringcolumn(name, "pickaxe", JsonUtil.toJson(opPlayer.getPickaxe()));
            MySQL.setDoulblecolumn(name, "blocks", Utils.fixDouble(opPlayer.getBlocks()));
            MySQL.setDoulblecolumn(name, "money", Utils.fixDouble(opPlayer.getMoney()));
            MySQL.setDoulblecolumn(name, "multiplier", Utils.fixDouble(opPlayer.getMultiplier()));
            MySQL.setDoulblecolumn(name, "prestige", Utils.fixDouble(opPlayer.getPrestige()));
            MySQL.setDoulblecolumn(name, "tokens", Utils.fixDouble(opPlayer.getTokens()));
            manager.remove(name);
        }
    }

    public boolean opPlayerExists(String name) {
        try {
            PreparedStatement statement = MySQL.connection().prepareStatement("SELECT * FROM " + MySQL.TABLE + " WHERE PlayerName=?");
            statement.setString(1, name);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return true;
            }
        } catch (SQLException var4) {
            SQLException e = var4;
            e.printStackTrace();
        }

        return false;
    }

    public void createOpPlayer(Player player) {
        try {
            PreparedStatement statement = MySQL.connection().prepareStatement("SELECT * FROM " + MySQL.TABLE + " WHERE PlayerName=?");
            statement.setString(1, player.getName());
            ResultSet results = statement.executeQuery();
            results.next();
            if (!this.opPlayerExists(player.getName())) {
                PreparedStatement insert = MySQL.connection().prepareStatement("INSERT INTO " + MySQL.TABLE + " (PlayerName,rank,pickaxe,blocks,money,multiplier,prestige,tokens) VALUES (?,?,?,?,?,?,?,?)");
                insert.setString(1, player.getName());
                insert.setInt(2, Ranks.A.ordinal());
                insert.setString(3, JsonUtil.toJson(new Pickaxe(player)));
                insert.setDouble(4, 0.0);
                insert.setDouble(5, 0.0);
                insert.setDouble(6, 0.0);
                insert.setDouble(7, 0.0);
                insert.setDouble(8, 0.0);
                insert.executeUpdate();
            }
        } catch (SQLException var5) {
            SQLException e = var5;
            e.printStackTrace();
        }

    }
}
