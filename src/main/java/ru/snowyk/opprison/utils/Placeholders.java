package ru.snowyk.opprison.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import ru.snowyk.opprison.OpPrison;
import ru.snowyk.opprison.api.OpPlayer;
import ru.snowyk.opprison.api.OpPlayerManager;

public class Placeholders extends PlaceholderExpansion {
    private OpPrison plugin;

    public Placeholders(OpPrison plugin) {
        this.plugin = plugin;
    }

    public boolean persist() {
        return true;
    }

    public boolean canRegister() {
        return true;
    }

    public String getAuthor() {
        return this.plugin.getDescription().getAuthors().toString();
    }

    public String getIdentifier() {
        return "OpPrison";
    }

    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    public String onPlaceholderRequest(Player player, String identifier) {
        OpPlayer opPlayer = OpPlayerManager.getOpPlayer(player.getName());
        if (identifier.equals("rank")) {
            return opPlayer.getRank().getName();
        } else if (identifier.equals("blocks")) {
            return Utils.getString(opPlayer.getBlocks());
        } else if (identifier.equals("money")) {
            return Utils.getString(opPlayer.getMoney());
        } else if (identifier.equals("multiplier")) {
            return Utils.getString(opPlayer.getMultiplier());
        } else if (identifier.equals("prestige")) {
            return Utils.getString(opPlayer.getPrestige());
        } else if (identifier.equals("tokens")) {
            return Utils.getString(opPlayer.getTokens());
        } else {
            return null;
        }
    }
}
