package ru.snowyk.opprison.regions;

public class Flags {
    private final boolean blockbreak;
    private final boolean blockplace;
    private final boolean pvp;

    public Flags(boolean blockbreak, boolean blockplace, boolean pvp) {
        this.blockbreak = blockbreak;
        this.blockplace = blockplace;
        this.pvp = pvp;
    }

    public boolean isBlockbreak() {
        return blockbreak;
    }

    public boolean isBlockplace() {
        return blockplace;
    }

    public boolean isPvp() {
        return pvp;
    }
}
