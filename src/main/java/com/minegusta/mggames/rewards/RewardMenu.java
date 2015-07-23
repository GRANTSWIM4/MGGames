package com.minegusta.mggames.rewards;

import com.google.common.collect.Lists;
import com.minegusta.mggames.kits.Kit;
import com.minegusta.mggames.kits.KitRegistry;
import com.minegusta.mggames.player.MGPlayer;
import com.minegusta.mggames.register.Register;
import com.minegusta.mggames.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class RewardMenu
{
    public static List<Inventory> invs = Lists.newArrayList();

    public static ItemStack clear = new ItemStack(Material.BARRIER, 1)
    {
        {
            ItemMeta meta = getItemMeta();
            meta.setDisplayName(ChatColor.RED + "Disable All");
            meta.setLore(Lists.newArrayList(ChatColor.LIGHT_PURPLE + "Click this to", ChatColor.LIGHT_PURPLE + "clear all actives."));
            setItemMeta(meta);
        }
    };

    public static void openRewardMenu(Player p)
    {
        //Create the inv
        Inventory inv = Bukkit.createInventory(p, 9 * 4, ChatColor.DARK_RED + "DG" + ChatColor.GRAY + "-" + ChatColor.DARK_AQUA + "MG " + ChatColor.LIGHT_PURPLE + "Perks");

        MGPlayer mgp = Register.getPlayer(p);

        //Fill the inv with unlockables that the player has

        int slot = 0;
        for(Unlockable u : mgp.getUnlockables())
        {
            if(u.getType() != UnlockableType.TOGGLE) continue;
            boolean active = mgp.getActiveUnlockables().contains(u);
            inv.setItem(slot, u.buildUnlockedItem(active));
            slot++;
        }

        //Set the item for clearing
        inv.setItem(35, clear);

        //Set the task + open the inv for the player
        p.openInventory(inv);
        ChatUtil.sendFormattedMessage(p, ChatColor.LIGHT_PURPLE + "You opened the reward menu.");
        invs.add(inv);
    }
}
