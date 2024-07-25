package ru.snowyk.opprison.api;

public enum Ranks {
    A("§a", 0.0),
    B("§b", 10000.0),
    C("§e", 50000.0),
    D("§d", 100000.0),
    E("§e", 200000.0),
    F("§7", 500000.0),
    G("§7", 1500000.0),
    H("§a", 4500000.0),
    I("§6", 7500000.0),
    J("§6", 1.0E7),
    K("§2", 1.5E7),
    L("§c", 2.5E7),
    M("§4", 3.5E7),
    N("§b", 5.0E7),
    O("§6", 7.5E7),
    P("§d", 1.0E8),
    Q("§c", 1.5E8),
    R("§1", 2.5E8),
    S("§b", 5.0E8),
    T("§e", 1.0E9),
    U("§e", 1.5E9),
    V("§d", 2.5E9),
    W("§c", 5.0E9),
    X("§3", 1.0E10),
    Y("§2", 2.5E10),
    Z("§d§l", 5.0E10);

    private final String color;
    private final String name;
    private final double price;

    private Ranks(String color, double price) {
        this.color = color;
        this.name = color + super.name();
        this.price = price;
    }

    public boolean has(Ranks rank) {
        return this.ordinal() >= rank.ordinal();
    }

    public static Ranks fromOrdinal(int ordinal) {
        return ordinal >= 0 && ordinal <= values().length - 1 ? values()[ordinal] : A;
    }

    public static Ranks fromNameOrOrdinal(String noo) {
        try {
            return fromOrdinal(Integer.parseInt(noo));
        } catch (NumberFormatException var4) {
            try {
                return valueOf(noo.toUpperCase());
            } catch (IllegalArgumentException var3) {
                return A;
            }
        }
    }

    public boolean isMax() {
        return this.ordinal() == values().length - 1;
    }

    public boolean isMin() {
        return this.ordinal() == 0;
    }

    public Ranks next() {
        return fromOrdinal(this.ordinal() + 1);
    }

    public String getColor() {
        return this.color;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }
}
