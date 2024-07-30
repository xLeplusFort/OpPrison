package ru.snowyk.opprison.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.snowyk.opprison.tools.pickaxe.Pickaxe;

@AllArgsConstructor
@Getter
@Setter
public class OpPlayer {
    Ranks rank;
    Pickaxe pickaxe;
    double blocks;
    double money;
    double multiplier;
    double prestige;
    double tokens;
}
