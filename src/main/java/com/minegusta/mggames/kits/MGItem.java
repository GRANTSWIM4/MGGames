package com.minegusta.mggames.kits;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MGItem
{
    private String name;
    private String lore;
    private Material material;
    private int amount;
    private int data;
    private int slot;

    public MGItem(String name, int slot, Material material, int amount, int data, String lore)
    {
        this.name = name;
        this.material = material;
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
            }
        };
    }

    public String getName()
    {
        return name;
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
