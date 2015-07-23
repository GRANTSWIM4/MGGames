package com.minegusta.mggames.rewards;

import com.google.common.collect.Lists;
import com.minegusta.mggames.kits.Kit;
import com.minegusta.mggames.kits.KitRegistry;
import com.minegusta.mggames.player.MGPlayer;
import com.minegusta.mggames.register.Register;
import com.minegusta.mggames.tasks.ShopTask;
import com.minegusta.mggames.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopMenu
{
    public static ItemStack kitShopTab = new ItemStack(Material.ENDER_CHEST, 1)
    {
        {
            ItemMeta meta = getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + "Kit Shop");
            meta.setLore(Lists.newArrayList(ChatColor.LIGHT_PURPLE + "Click this to open", ChatColor.LIGHT_PURPLE + "the kit shop."));
            setItemMeta(meta);
        }
    };
    public static ItemStack unlockableShopTab = new ItemStack(Material.DIAMOND, 1)
    {
        {
            ItemMeta meta = getItemMeta();
            meta.setDisplayName(ChatColor.DARK_AQUA + "Unlockables Shop");
            meta.setLore(Lists.newArrayList(ChatColor.LIGHT_PURPLE + "Click this to open", ChatColor.LIGHT_PURPLE + "the unlockables shop."));
            setItemMeta(meta);
        }
    };

    private static ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1)
    {
        {
            ItemMeta meta = getItemMeta();
            meta.setDisplayName(" ");
            meta.setLore(Lists.newArrayList(" "));
            setItemMeta(meta);
        }
    };

    public static ItemStack getPane(int data)
    {
        ItemStack stack = pane.clone();
        stack.setDurability((byte) data);

        return stack;
    }

    public static void openShop(Player p, boolean message)
    {
        //Create the inv
        Inventory inv = Bukkit.createInventory(p, 9 * 5, ChatColor.DARK_RED + "DG" + ChatColor.GRAY + "-" + ChatColor.DARK_AQUA + "MG " + ChatColor.LIGHT_PURPLE + "Shop");

        //Fill the inv with unlockables that the player does not have already.

        for(int i = 0; i < 9; i++)
        {
            inv.setItem(i, getPane(i));
        }

        int data = 0;
        for(int i = 36; i < 45; i++)
        {
            inv.setItem(i, getPane(data));
            data++;
        }

        MGPlayer mgp = Register.getPlayer(p);

        int slot = 9;
        for(Unlockable u : Unlockable.values())
        {
            if(mgp.hasUnlockable(u))continue;
            inv.setItem(slot, u.buildShopItem());
            slot++;
        }

        //Set the item for the kit tab.
        inv.setItem(35, kitShopTab);

        //Set the task + open the inv for the player
        p.openInventory(inv);
        if(message)ChatUtil.sendFormattedMessage(p, ChatColor.LIGHT_PURPLE + "You have " + ChatColor.DARK_PURPLE + Register.getPlayer(p).getTickets() + ChatColor.LIGHT_PURPLE + " tickets.", ChatColor.GRAY + "Use " + ChatColor.YELLOW +  "/Rewards " + ChatColor.GRAY + "to select your active ones.");
        ShopTask.addInventory(inv);
    }

    public static void openKitShop(Player p, boolean message)
    {
        //Create the inv
        Inventory inv = Bukkit.createInventory(p, 9 * 5, ChatColor.DARK_RED + "DG" + ChatColor.GRAY + "-" + ChatColor.DARK_AQUA + "MG " + ChatColor.LIGHT_PURPLE + "Kits");

        MGPlayer mgp = Register.getPlayer(p);

        //Fill the inv with unlockables that the player has

        for(int i = 0; i < 9; i++)
        {
            inv.setItem(i, getPane(i));
        }

        int data = 0;
        for(int i = 36; i < 45; i++)
        {
            inv.setItem(i, getPane(data));
            data++;
        }

        int slot = 9;
        for(Kit kit : KitRegistry.getKits())
        {
            if(mgp.hasKit(kit.getName()) || kit.isDefault()) continue;
            inv.setItem(slot, getKitItem(kit.getName(), kit.getCost()));
            slot++;
        }

        //Set the item for the normal tab.
        inv.setItem(35, unlockableShopTab);

        //Set the task + open the inv for the player
        p.openInventory(inv);
        if(message)ChatUtil.sendFormattedMessage(p, ChatColor.LIGHT_PURPLE + "You opened the kit menu.");
        ShopTask.addInventory(inv);
    }

    public static void update(Inventory i)
    {
        for(ItemStack item : i.getContents())
        {
            if(item != null && item.getType() == Material.STAINED_GLASS_PANE)
            {
                int data = item.getDurability();
                data++;
                if(data > 15) data = 0;
                item.setDurability((short)data);
            }
        }
    }

    private static ItemStack getKitItem(String name, int cost)
    {
        return new ItemStack(Material.TRAPPED_CHEST, 1)
        {
            {
                ItemMeta meta = getItemMeta();
                meta.setDisplayName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + name);
                meta.setLore(Lists.newArrayList(ChatColor.GREEN + "Click this to", ChatColor.GREEN + "unlock " + name + ".", ChatColor.LIGHT_PURPLE + "Cost: " + ChatColor.DARK_PURPLE + cost + " Tickets"));
                setItemMeta(meta);
            }
        };
    }
}
