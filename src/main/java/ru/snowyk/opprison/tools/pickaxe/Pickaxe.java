package ru.snowyk.opprison.tools.pickaxe;

import org.bukkit.entity.Player;
import ru.snowyk.opprison.utils.Utils;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Pickaxe {
    private String name;
    private ConcurrentMap<PickaxeEnchant, Integer> enchants = new ConcurrentHashMap();

    public Pickaxe(Player owner) {
        this.name = Utils.color("&bКирка " + owner.getName());
        Arrays.stream(PickaxeEnchant.values()).filter((e) -> {
            return e.getLevel() > 0;
        }).forEach((e) -> {
            Integer var10000 = (Integer)this.enchants.put(e, e.getLevel());
        });
    }

    public int getEnchant(PickaxeEnchant e) {
        return (Integer)this.enchants.getOrDefault(e, 0);
    }

    public String getName() {
        return name;
    }

    public ConcurrentMap<PickaxeEnchant, Integer> getEnchants() {
        return enchants;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEnchants(ConcurrentMap<PickaxeEnchant, Integer> enchants) {
        this.enchants = enchants;
    }
}
