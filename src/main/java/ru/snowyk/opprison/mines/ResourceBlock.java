package ru.snowyk.opprison.mines;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Material;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Getter
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
}
