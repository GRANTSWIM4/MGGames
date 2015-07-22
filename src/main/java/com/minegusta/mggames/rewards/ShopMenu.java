package com.minegusta.mggames.rewards;

import com.minegusta.mggames.register.Register;
import com.minegusta.mggames.tasks.ShopTask;
import com.minegusta.mggames.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ShopMenu
{
    public static void openShop(Player p)
    {
        //Create the inv
        Inventory inv = Bukkit.createInventory(p, 9 * 5, ChatColor.DARK_RED + "DG" + ChatColor.GRAY + "-" + ChatColor.DARK_AQUA + "MG " + ChatColor.LIGHT_PURPLE + "Shop");

        //Fill the inv with unlockables that the player does not have already.

        for(int i = 0; i < 9; i++)
        {
            inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 0, (short) i));
        }

        int data = 0;
        for(int i = 36; i < 45; i++)
        {
            inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 0, (short) data));
            data++;
        }

        int slot = 9;
        for(Unlockable u : Unlockable.values())
        {
            inv.setItem(slot, u.buildShopItem());
            slot++;
        }

        //Set the task + open the inv for the player
        p.openInventory(inv);
        ChatUtil.sendFormattedMessage(p, ChatColor.LIGHT_PURPLE + "You have " + ChatColor.DARK_PURPLE + Register.getPlayer(p).getTickets() + ChatColor.LIGHT_PURPLE + " tickets.", "Use " + ChatColor.YELLOW +  "/Rewards " + ChatColor.GRAY + "to select your active ones.");
        ShopTask.addInventory(inv);
    }

    public static void update(Inventory i)
    {
        for(ItemStack item : i.getContents())
        {
            if(item != null && item.getType() == Material.STAINED_GLASS_PANE)
            {
                int data = item.getDurability();
                if(data == 15) data = 0;
                item.setDurability((short)data);
            }
        }
    }
}
