package ru.snowyk.opprison.mines;

import io.netty.util.internal.ConcurrentSet;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import ru.snowyk.opprison.OpPrison;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MineManager {
    private final Set<Mine> mines = new ConcurrentSet();
    private final BukkitTask updater;

    public MineManager() {
        this.updater = Bukkit.getScheduler().runTaskTimer(OpPrison.instance, this::update, 20L, 20L);
    }

    public void update() {
        this.mines.stream().filter(Mine::expired).forEach(Mine::update);
    }

    public void clearMines() {
        this.mines.clear();
    }

    public void addMines(Mine... mines) {
        this.addMines(Arrays.asList(mines));
    }

    public void addMines(List<Mine> mines) {
        this.mines.addAll(mines);
        mines.forEach(Mine::update);
    }

    public Mine byId(String id) {
        Iterator var2 = this.mines.iterator();

        Mine mine;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            mine = (Mine)var2.next();
        } while(!mine.getId().equals(id));

        return mine;
    }

    public Set<Mine> getMines() {
        return this.mines;
    }
}
