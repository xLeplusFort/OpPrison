package ru.snowyk.opprison.scoreboard;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.snowyk.opprison.ConfigManager;
import ru.snowyk.opprison.utils.Utils;

import java.util.*;

public class ScoreBoard implements Listener {
    private static final Map<UUID, BPlayerBoard> boards = new HashMap();
    static List<String> l1 = new ArrayList();

    public static void createScoreBoard(Player p) {
        BPlayerBoard board = Netherboard.instance().createBoard(p, Utils.color(ConfigManager.getInstance().getConfig().getString("scoreboard-name")));
        boards.put(p.getUniqueId(), board);
    }

    public static void updateScoreBoard(BPlayerBoard board) {
        List<String> list = PlaceholderAPI.setPlaceholders(board.getPlayer(), Utils.color(ConfigManager.getInstance().getConfig().getStringList("scoreboard")));
        int reverseArrayCounter = list.size() - 1;

        int i;
        for(i = reverseArrayCounter; i >= 0; --i) {
            l1.add(list.get(i));
        }

        for(i = 0; i < l1.size(); ++i) {
            board.set((String)l1.get(i), i);
        }

        l1.clear();
    }

    public static void removeScoreBoard(Player p) {
        BPlayerBoard board = (BPlayerBoard)boards.remove(p.getUniqueId());
        if (board != null) {
            board.clear();
            board.delete();
        }

    }

    public static Map<UUID, BPlayerBoard> getBoards() {
        return boards;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!getBoards().containsKey(event.getPlayer().getUniqueId())) {
            createScoreBoard(event.getPlayer());
        }

    }

    @EventHandler
    public void onExit(PlayerQuitEvent event) {
        removeScoreBoard(event.getPlayer());
    }
}
