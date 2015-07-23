package com.minegusta.mggames.rewards;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public interface UnlockableItem
{
    void apply(Player p);

    void apply(Event event);

    UnlockableType getType();

    String getName();

    String[] getLore();

    int getCost();

    Material getMaterial();

    int getData();

    default ItemStack buildShopItem()
    {
        return new ItemStack(getMaterial(), 1, (byte) getData())
        {
            {
                ItemMeta meta = getItemMeta();
                meta.setDisplayName(getName());
                List<String> loreList = Lists.newArrayList();

                loreList.add(ChatColor.LIGHT_PURPLE + "Cost: " + ChatColor.DARK_PURPLE + getCost() + " Tickets");

                for(String s : getLore())
                {
                    loreList.add(ChatColor.YELLOW + s);
                }

                setItemMeta(meta);
            }
        };
    }

    default ItemStack buildUnlockedItem(boolean active)
    {
        return new ItemStack(getMaterial(), 1, (byte)getData())
        {
            {
                ItemMeta meta = getItemMeta();
                meta.setDisplayName(getName());
                List<String> loreList = Lists.newArrayList();
                ChatColor color = ChatColor.DARK_RED;
                if(active) color = ChatColor.DARK_GREEN;

                loreList.add(ChatColor.LIGHT_PURPLE + "Active: " + color + "" + ChatColor.BOLD + Boolean.toString(active));

                for(String s : getLore())
                {
                    loreList.add(ChatColor.YELLOW + s);
                }

                setItemMeta(meta);
            }
        };
    }
}
