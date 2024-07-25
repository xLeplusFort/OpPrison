package ru.snowyk.opprison.api;

import ru.snowyk.opprison.tools.pickaxe.Pickaxe;

public class OpPlayer {
    Ranks rank;
    Pickaxe pickaxe;
    double blocks;
    double money;
    double multiplier;
    double prestige;
    double tokens;

    public OpPlayer(Ranks rank, Pickaxe pickaxe, double blocks, double money, double multiplier, double prestige, double tokens) {
        this.rank = rank;
        this.pickaxe = pickaxe;
        this.blocks = blocks;
        this.money = money;
        this.multiplier = multiplier;
        this.prestige = prestige;
        this.tokens = tokens;
    }

    public Ranks getRank() {
        return this.rank;
    }

    public void setRank(Ranks rank) {
        this.rank = rank;
    }

    public Pickaxe getPickaxe() {
        return this.pickaxe;
    }

    public void setPickaxe(Pickaxe pickaxe) {
        this.pickaxe = pickaxe;
    }

    public double getBlocks() {
        return this.blocks;
    }

    public void setBlocks(double blocks) {
        this.blocks = blocks;
    }

    public double getMoney() {
        return this.money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getMultiplier() {
        return this.multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public double getPrestige() {
        return this.prestige;
    }

    public void setPrestige(double prestige) {
        this.prestige = prestige;
    }

    public double getTokens() {
        return this.tokens;
    }

    public void setTokens(double tokens) {
        this.tokens = tokens;
    }
}
