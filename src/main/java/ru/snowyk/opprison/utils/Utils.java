package ru.snowyk.opprison.utils;

import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import ru.snowyk.opprison.OpPrison;
import ru.snowyk.opprison.api.OpPlayer;
import ru.snowyk.opprison.regions.Global;
import ru.snowyk.opprison.regions.Region;
import ru.snowyk.opprison.tools.pickaxe.PickaxeEnchant;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class Utils {
    protected static final Random rnd = ThreadLocalRandom.current();

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> color(List<String> msg) {
        List<String> list = new ArrayList();
        Iterator var2 = msg.iterator();

        while(var2.hasNext()) {
            String a = (String)var2.next();
            list.add(ChatColor.translateAlternateColorCodes('&', a));
        }

        return list;
    }

    public static double fixDouble(double amount, int digits) {
        if (digits == 0) {
            return (double)((int)amount);
        } else {
            StringBuilder format = new StringBuilder("##");

            for(int i = 0; i < digits; ++i) {
                if (i == 0) {
                    format.append(".");
                }

                format.append("#");
            }

            return Double.parseDouble((new DecimalFormat(format.toString())).format(amount).replace(",", "."));
        }
    }

    public static double fixDouble(double amount) {
        return fixDouble(amount, 2);
    }

    public static String getString(double amount) {
        double value;
        String s;
        if (amount >= 1.0E12) {
            value = amount / 1.0E12;
            s = fixDouble(value) + "T";
        } else if (amount >= 1.0E9) {
            value = amount / 1.0E9;
            s = fixDouble(value) + "B";
        } else if (amount >= 1000000.0) {
            value = amount / 1000000.0;
            s = fixDouble(value) + "M";
        } else if (amount >= 1000.0) {
            value = amount / 1000.0;
            s = fixDouble(value) + "K";
        } else {
            s = (int) amount + "";
        }

        return s;
    }

    public static final Function<Double, Double> PRESTIGE = (l) -> {
        return 1.0E9 * (l == 1.0 ? 1.0 : l * 1.10);
    };

    public static double getBlockPrice(OpPlayer player, Block breaked) {
        double lucky = (double)player.getPickaxe().getEnchant(PickaxeEnchant.LUCKY);
        double price = (lucky * 0.01) * (double)(player.getRank().ordinal() + 1) + 100.0;
        return (price + player.getMultiplier() * price);
    }

    public static double getBlockTokens(OpPlayer player) {
        double merchant = (double)player.getPickaxe().getEnchant(PickaxeEnchant.TOKEN_MERCHANT);
        double tokens = (merchant * 0.01) * 1 + 100.0;
        return tokens;
    }

    public static boolean breakLayer(OpPlayer player, BlockBreakEvent e) {
        if (player.getPickaxe().getEnchant(PickaxeEnchant.DESTROYER) != 0 && rnd.nextInt(5000) <= player.getPickaxe().getEnchant(PickaxeEnchant.DESTROYER)) {
            Region rg = OpPrison.instance.regionManager.getPrimaryRegion(e.getBlock().getLocation());
            if (rg instanceof Global) {
                return false;
            } else {
                V3 min = rg.getMin();
                V3 max = rg.getMax();
                World world = rg.getWorld();
                int y = e.getBlock().getY();
                Bukkit.getScheduler().runTaskLater(OpPrison.instance, () -> {
                    AtomicReference<Double> money = new AtomicReference(0.0);
                    AtomicReference<Double> tokens = new AtomicReference(0.0);

                    for(int x = min.getX(); x <= max.getX(); ++x) {
                        for(int z = min.getZ(); z <= max.getZ(); ++z) {
                            Block b = world.getBlockAt(x, y, z);
                            if (b.getType() != Material.AIR) {
                                b.setType(Material.AIR);

                                money.updateAndGet((v) -> {
                                    return v + getBlockPrice(player, b) / 1.2;
                                });
                                tokens.updateAndGet((v) -> {
                                    return v + getBlockTokens(player);
                                });
                            }
                        }
                    }

                    player.setMoney(player.getMoney() + (Double)money.get());
                    player.setTokens(player.getTokens() + (Double)tokens.get());
                    world.playSound(e.getBlock().getLocation(), Sound.BLOCK_STONE_BREAK, 0.7F, 1.0F);
                }, 1L);
                return true;
            }
        } else {
            return false;
        }
    }

    public static HashMap<String, Double> calculatePercents(HashMap<String, Integer> attackers, double damage) {
        HashMap<String, Double> percents = new HashMap();
        Iterator var4 = attackers.entrySet().iterator();

        while(var4.hasNext()) {
            Map.Entry<String, Integer> entry = (Map.Entry)var4.next();
            percents.put(entry.getKey(), (double)(Integer)entry.getValue() / damage);
        }

        return percents;
    }

    public static void sendActionBar(Player p, String boss, float health) {
        String message = color(String.format("&7Здоровье %s &c&l%s❤", boss, (int)health));
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, ChatMessageType.a((byte)2), p.getUniqueId());
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(ppoc);
    }
}
