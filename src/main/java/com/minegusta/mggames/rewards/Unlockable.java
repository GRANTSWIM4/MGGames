package com.minegusta.mggames.rewards;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public enum Unlockable
{
    UNLOCKABLE_ITEM_1("name", Material.EMERALD_BLOCK, 0, 100, "lol");

    private String[] lore;
    private String name;
    private Material material;
    private  int data;
    private int tickets;

    Unlockable(String name, Material material, int data, int tickets, String... lore)
    {
        this.name = name;
        this.material = material;
        this.data = data;
        this.tickets = tickets;
        this.lore = lore;
    }

    public ItemStack buildShopItem()
    {
        return new ItemStack(material, 1, (byte)data)
        {
            {
                ItemMeta meta = getItemMeta();
                meta.setDisplayName(name());
                List<String> loreList = Lists.newArrayList();

                loreList.add(ChatColor.LIGHT_PURPLE + "Cost: " + ChatColor.LIGHT_PURPLE + getCost());

                for(String s : lore)
                {
                    loreList.add(s);
                }

                setItemMeta(meta);
            }
        };
    }

    public ItemStack buildUnlockedItem(boolean active)
    {
        return new ItemStack(material, 1, (byte)data)
        {
            {
                ItemMeta meta = getItemMeta();
                meta.setDisplayName(name());
                List<String> loreList = Lists.newArrayList();
                ChatColor color = ChatColor.DARK_RED;
                if(active) color = ChatColor.DARK_GREEN;

                loreList.add(ChatColor.LIGHT_PURPLE + "Active: " + color + "" + ChatColor.BOLD + Boolean.toString(active));

                for(String s : lore)
                {
                    loreList.add(s);
                }

                setItemMeta(meta);
            }
        };
    }

    public String getName()
    {
        return name();
    }

    public int getCost()
    {
        return tickets;
    }
}
