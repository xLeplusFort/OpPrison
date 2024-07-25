package ru.snowyk.opprison.mines;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Material;

public class ResourceBlock {
    private final Material material;
    private final int chance;

    public static ResourceBlock fromString(String in) {
        try {
            String[] spl = in.split(":");
            if (spl.length >= 1 && spl.length <= 2) {
                if (spl.length == 1) {
                    spl = (String[])((String[])ArrayUtils.add(spl, "100"));
                }

                int chance = parseInt(spl[1]);
                Material material = Material.matchMaterial(spl[0]);
                return chance != -1 && material != null ? builder().material(material).chance(chance).build() : null;
            } else {
                return null;
            }
        } catch (Throwable var5) {
            return null;
        }
    }

    public static List<ResourceBlock> fromList(List<String> blocks) {
        return (List)blocks.stream().map(ResourceBlock::fromString).filter(Objects::nonNull).collect(Collectors.toList());
    }

    protected static int parseInt(String in) {
        int result = 0;
        try {
            result = Integer.parseInt(in);
        }
        catch (Throwable e) {
            result = -1;
        }
        return result;
    }

    public ResourceBlock(Material material, int chance) {
        this.material = material;
        this.chance = chance;
    }

    public static ResourceBlockBuilder builder() {
        return new ResourceBlockBuilder();
    }

    public Material getMaterial() {
        return this.material;
    }

    public int getChance() {
        return this.chance;
    }

    public static class ResourceBlockBuilder {
        private Material material;
        private int chance;

        public ResourceBlockBuilder material(Material material) {
            this.material = material;
            return this;
        }

        public ResourceBlockBuilder chance(int chance) {
            this.chance = chance;
            return this;
        }

        public ResourceBlock build() {
            return new ResourceBlock(this.material, this.chance);
        }
    }
}
