package com.minegusta.mggames.kits;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.Map;

public class MGItem
{
    private String name;
    private String lore;
    private Material material;
    private int amount;
    private int data;
    private int slot;
    private Map<Enchantment, Integer> enchantments;

    public MGItem(String name, int slot, Material material, int amount, int data, String lore, Map<Enchantment, Integer> enchantments)
    {
        this.name = name;
        this.material = material;
        this.enchantments = enchantments;
        this.slot = slot;
        this.amount = amount;
        this.data = data;
        this.lore = lore;
    }

    public ItemStack buildItem()
    {
        ItemStack i;
        if(material == Material.POTION)
        {
            Potion potion = new Potion(PotionType.getByDamageValue(data));
            if(data > 16000) potion.setSplash(true);

            i = potion.toItemStack(amount);
        }
        else
        {
            i = new ItemStack(material, amount, (byte) data)
            {
                {
                    ItemMeta meta = getItemMeta();
                    meta.setLore(Lists.newArrayList(lore));
                    meta.setDisplayName(name);
                    setItemMeta(meta);
                }
            };
        }

        enchantments.keySet().stream().forEach(ench -> i.addUnsafeEnchantment(ench, enchantments.get(ench)));
        return i;
    }

    public String getName()
    {
        return name;
    }

    public Map<Enchantment, Integer> getEnchantments()
    {
        return enchantments;
    }

    public int getData()
    {
        return data;
    }

    public String getLore()
    {
        return lore;
    }

    public int getAmount()
    {
        return amount;
    }

    public Material getMaterial()
    {
        return material;
    }



    public MGItem setSlot(int slot)
    {
        this.slot = slot;
        return this;
    }

    public int getSlot()
    {
        return slot;
    }
}
