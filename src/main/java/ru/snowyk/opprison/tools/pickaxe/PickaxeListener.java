package ru.snowyk.opprison.tools.pickaxe;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import ru.snowyk.opprison.api.OpPlayer;
import ru.snowyk.opprison.api.OpPlayerManager;
import ru.snowyk.opprison.utils.ItemBuilder;
import ru.snowyk.opprison.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class PickaxeListener implements Listener {

    @EventHandler
    public void handle(PlayerSwapHandItemsEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void handle(InventoryClickEvent e) {
        ItemStack hot = e.getHotbarButton() > -1 && e.getHotbarButton() <= e.getWhoClicked().getInventory().getContents().length ? e.getWhoClicked().getInventory().getContents()[e.getHotbarButton()] : null;
        if (this.isPickaxe(e.getCurrentItem()) || this.isPickaxe(e.getCursor()) || this.isPickaxe(hot) || e.getClickedInventory() != null && this.isPickaxe(e.getClickedInventory().getItem(e.getSlot())) || this.isPickaxe(e.getInventory().getItem(e.getSlot()))) {
            e.setCancelled(true);
        }

    }

    // Лень было заморачиваться и сделал тупые проверки через название кирки

    @EventHandler
    public void drop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    protected boolean isPickaxe(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().getDisplayName().contains("Кирка");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        OpPlayer opPlayer = OpPlayerManager.getOpPlayer(player.getName());
        ItemStack itemStack = new ItemBuilder(Material.GOLDEN_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 500).setName(opPlayer.getPickaxe().getName()).setUnbreakable(true).setLore((List)opPlayer.getPickaxe().getEnchants().entrySet().stream().filter((e) -> {
            return (Integer)e.getValue() > 0;
        }).map((e) -> {
            return " " + Utils.color(((PickaxeEnchant)e.getKey()).getName() + " " + e.getValue());
        }).collect(Collectors.toList())).toItemStack();
        if (player.getInventory().getItem(0) == null) {
            player.getInventory().setItem(0, itemStack);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (!event.isCancelled()) {
            event.setDropItems(false);
            event.setExpToDrop(0);
            OpPlayer opPlayer = OpPlayerManager.getOpPlayer(event.getPlayer().getName());
            opPlayer.setBlocks(opPlayer.getBlocks() + 1.0);
            if (event.getPlayer().getInventory().getItemInMainHand().hasItemMeta() && event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Кирка")) {
                Utils.breakLayer(opPlayer, event);
            }
        }
    }
}
