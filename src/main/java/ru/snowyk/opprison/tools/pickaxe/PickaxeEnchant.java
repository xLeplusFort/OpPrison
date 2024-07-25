package ru.snowyk.opprison.tools.pickaxe;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum PickaxeEnchant {
    LUCKY("&aУдача", 0, 1, 1700, 50000, Material.GOLDEN_PICKAXE, new String[]{"Шанс получить увеличенный дроп при копании"}),
    TOKEN_MERCHANT("&aЖадность токенов", 0, 1, 500, 5000, Material.MAGMA_CREAM, new String[]{"Бустер токенов при копании"}),
    DESTROYER("&aРазрушитель", 0, 1, 5000, 5000, Material.DIAMOND_PICKAXE, new String[]{"Шанс сломать весь слой при копании"});

    private final String name;
    private final int needprestige;
    private final int level;
    private final double price;
    private final int maxlevel;
    private final Material material;
    private final String[] desc;

    public static List<PickaxeEnchant> get() {
        return (List)Arrays.stream(values()).collect(Collectors.toList());
    }

    private PickaxeEnchant(String name, int needprestige, int level, double price, int maxlevel, Material material, String[] desc) {
        this.name = name;
        this.needprestige = needprestige;
        this.level = level;
        this.price = price;
        this.maxlevel = maxlevel;
        this.material = material;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public int getNeedprestige() {
        return needprestige;
    }

    public int getLevel() {
        return level;
    }

    public double getPrice() {
        return price;
    }

    public int getMaxlevel() {
        return maxlevel;
    }

    public Material getMaterial() {
        return material;
    }

    public String[] getDesc() {
        return desc;
    }
}
