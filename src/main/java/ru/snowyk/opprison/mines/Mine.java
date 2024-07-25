package ru.snowyk.opprison.mines;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import ru.snowyk.opprison.OpPrison;
import ru.snowyk.opprison.regions.Region;
import ru.snowyk.opprison.utils.V3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mine {
    private static final Random RND = new Random();
    private final String id;
    private final List<Region> regions;
    private final List<ResourceBlock> content;
    private final ResourceBlock upperContent;
    private final int update;
    private long nextUpdate = 0L;

    public Mine(String id, List<Region> regions, List<ResourceBlock> content, ResourceBlock upperContent, int update, long nextUpdate) {
        this.id = id;
        this.regions = regions;
        this.content = content;
        this.upperContent = upperContent;
        this.update = update;
        this.nextUpdate = nextUpdate;
    }

    public void update() {
        this.fillAll();
        this.nextUpdate = System.currentTimeMillis() + (long)this.update * 1000L;
    }

    public boolean expired() {
        return System.currentTimeMillis() >= this.nextUpdate;
    }

    public void fill(Region rg) {
        World world = rg.getWorld();
        V3 min = rg.getMin();
        V3 max = rg.getMax();
        ArrayList<Integer> ys = new ArrayList<Integer>();
        block0: for (int y = min.getY(); y <= max.getY(); ++y) {
            for (int x = min.getX(); x <= max.getX(); ++x) {
                for (int z = min.getZ(); z <= max.getZ(); ++z) {
                    if (world.getBlockAt(x, y, z).getType() != Material.AIR) continue;
                    ys.add(y);
                    continue block0;
                }
            }
        }
        ArrayList<Block> oneTask = new ArrayList<Block>();
        ArrayList<Block> twoTask = new ArrayList<Block>();
        for (int y = max.getY(); y >= min.getY(); --y) {
            for (int x = min.getX(); x <= max.getX(); ++x) {
                for (int z = min.getZ(); z <= max.getZ(); ++z) {
                    if (!ys.contains(y)) continue;
                    Block block = world.getBlockAt(x, y, z);
                    if ((max.getY() - min.getY()) / 2 + min.getY() >= y) {
                        twoTask.add(block);
                        continue;
                    }
                    oneTask.add(block);
                }
            }
        }
        this.runFill(oneTask, max.getY(), 1);
        rg.getPlayers().forEach(p -> {
            p.teleport(new Location(p.getWorld(), p.getLocation().getX(), max.getY() + 1, p.getLocation().getZ()));
            p.setVelocity(p.getVelocity().setY(p.getVelocity().getY() + 0.7));
        });
        this.runFill(twoTask, max.getY(), 1);
    }

    protected void runFill(List<Block> blocks, int maxY, int delay) {
        Bukkit.getScheduler().runTaskLater(OpPrison.instance, () -> {
            blocks.forEach((block) -> {
                for(int i = 0; i < this.content.size(); ++i) {
                    ResourceBlock resourceBlock = (ResourceBlock) this.content.get(i);
                    if (resourceBlock.getChance() >= RND.nextInt(101) || i == this.content.size() - 1) {
                        block.setType(resourceBlock.getMaterial(), false);
                        break;
                    }
                }

                if (this.upperContent != null && block.getY() == maxY) {
                    block.setType(this.upperContent.getMaterial(), false);
                }

            });
        }, 1L);
    }

    public void fillAll() {
        this.regions.forEach(this::fill);
    }

    private static ResourceBlock $default$upperContent() {
        return null;
    }

    public static MineBuilder builder() {
        return new MineBuilder();
    }

    public String getId() {
        return id;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public List<ResourceBlock> getContent() {
        return content;
    }

    public ResourceBlock getUpperContent() {
        return upperContent;
    }

    public int getUpdate() {
        return update;
    }

    public long getNextUpdate() {
        return nextUpdate;
    }

    public static class MineBuilder {
        private String id;
        private List<Region> regions;
        private List<ResourceBlock> content;
        private boolean upperContent$set;
        private ResourceBlock upperContent$value;
        private int update;
        private long nextUpdate;

        MineBuilder() {
        }

        public MineBuilder id(String id) {
            this.id = id;
            return this;
        }

        public MineBuilder regions(List<Region> regions) {
            this.regions = regions;
            return this;
        }

        public MineBuilder content(List<ResourceBlock> content) {
            this.content = content;
            return this;
        }

        public MineBuilder upperContent(ResourceBlock upperContent) {
            this.upperContent$value = upperContent;
            this.upperContent$set = true;
            return this;
        }

        public MineBuilder update(int update) {
            this.update = update;
            return this;
        }

        public MineBuilder nextUpdate(long nextUpdate) {
            this.nextUpdate = nextUpdate;
            return this;
        }

        public Mine build() {
            ResourceBlock upperContent$value = this.upperContent$value;
            if (!this.upperContent$set) {
                upperContent$value = Mine.$default$upperContent();
            }

            return new Mine(this.id, this.regions, this.content, upperContent$value, this.update, this.nextUpdate);
        }
    }
}
