package com.minegusta.mggames.kits;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class MGItem
{
    private String name;
    private String lore;
    private Material material;
    private int amount;
    private int data;
    private int slot;
    private Map<Enchantment, Integer> enchantments = Maps.newHashMap();

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
        return new ItemStack(material, amount, (byte) data)
        {
            {
                ItemMeta meta = getItemMeta();
                meta.setLore(Lists.newArrayList(lore));
                meta.setDisplayName(name);
                setItemMeta(meta);

                for(Enchantment i : getEnchantments().keySet())
                {
                    addUnsafeEnchantment(i, getEnchantments().get(i));
                }

            }
        };
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
