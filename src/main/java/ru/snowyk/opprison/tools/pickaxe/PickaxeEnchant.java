package ru.snowyk.opprison.tools.pickaxe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@AllArgsConstructor
@Getter
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
}
