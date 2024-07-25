package ru.snowyk.opprison.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class ItemBuilder {
    private ItemStack is;

    public ItemBuilder(Material m) {
        this(m, 1);
    }

    public ItemBuilder(ItemStack is) {
        this.is = is;
    }

    public ItemBuilder(Material m, int amount) {
        this.is = new ItemStack(m, amount);
    }

    public ItemBuilder setFlag(ItemFlag... flag) {
        ItemMeta meta = this.is.getItemMeta();
        meta.addItemFlags(flag);
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setPotion(PotionType potionType) {
        PotionMeta meta = (PotionMeta)this.is.getItemMeta();
        meta.setBasePotionData(new PotionData(potionType));
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder(Material m, int amount, byte durability) {
        this.is = new ItemStack(m, amount, (short)durability);
    }

    public ItemBuilder setSkull(String url) {
        if (url.isEmpty()) {
            return this;
        } else {
            ItemMeta isMeta = this.is.getItemMeta();
            GameProfile profile = new GameProfile(UUID.randomUUID(), (String)null);
            byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
            profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
            Field profileField = null;

            try {
                profileField = isMeta.getClass().getDeclaredField("profile");
            } catch (SecurityException | NoSuchFieldException var8) {
                Exception ex7 = var8;
                ex7.printStackTrace();
            }

            profileField.setAccessible(true);

            try {
                profileField.set(isMeta, profile);
            } catch (IllegalAccessException | IllegalArgumentException var7) {
                Exception ex8 = var7;
                ex8.printStackTrace();
            }

            this.is.setItemMeta(isMeta);
            return this;
        }
    }

    public ItemBuilder clone() {
        return new ItemBuilder(this.is);
    }

    public ItemBuilder setDurability(short dur) {
        this.is.setDurability(dur);
        return this;
    }

    public ItemBuilder setAuthor(String Author) {
        BookMeta bm = (BookMeta)this.is.getItemMeta();
        bm.setAuthor(Author);
        this.is.setItemMeta(bm);
        return this;
    }

    public ItemBuilder setTitle(String Title) {
        BookMeta bm = (BookMeta)this.is.getItemMeta();
        bm.setTitle(Title);
        this.is.setItemMeta(bm);
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta im = this.is.getItemMeta();
        im.setDisplayName(name);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level) {
        this.is.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment ench) {
        this.is.removeEnchantment(ench);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        try {
            SkullMeta im = (SkullMeta)this.is.getItemMeta();
            im.setOwner(owner);
            this.is.setItemMeta(im);
        } catch (ClassCastException var3) {
        }

        return this;
    }

    public ItemBuilder addEnchant(Enchantment ench, int level) {
        this.is.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
        this.is.addEnchantments(enchantments);
        return this;
    }

    public ItemBuilder setInfinityDurability() {
        this.is.setDurability((short)32767);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        ItemMeta im = this.is.getItemMeta();
        im.setLore(Arrays.asList(lore));
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta im = this.is.getItemMeta();
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder removeLoreLine(String line) {
        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList(im.getLore());
        if (!lore.contains(line)) {
            return this;
        } else {
            lore.remove(line);
            im.setLore(lore);
            this.is.setItemMeta(im);
            return this;
        }
    }

    public ItemBuilder removeLoreLine(int index) {
        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList(im.getLore());
        if (index >= 0 && index <= lore.size()) {
            lore.remove(index);
            im.setLore(lore);
            this.is.setItemMeta(im);
            return this;
        } else {
            return this;
        }
    }

    public ItemBuilder addLoreLine(String line) {
        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList();
        if (im.hasLore()) {
            lore = new ArrayList(im.getLore());
        }

        lore.add(line);
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addLoreLine(String line, int pos) {
        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList(im.getLore());
        lore.set(pos, line);
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    /** @deprecated */
    public ItemBuilder setLeatherArmorColor(Color color) {
        try {
            LeatherArmorMeta im = (LeatherArmorMeta)this.is.getItemMeta();
            im.setColor(color);
            this.is.setItemMeta(im);
        } catch (ClassCastException var3) {
        }

        return this;
    }

    public ItemStack toItemStack() {
        return this.is;
    }

    public ItemBuilder setUnbreakable(boolean b) {
        ItemMeta im = this.is.getItemMeta();
        im.setUnbreakable(b);
        this.is.setItemMeta(im);
        return this;
    }
}
