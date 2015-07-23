package com.minegusta.mggames.tasks;

import com.google.common.collect.Lists;
import com.minegusta.mggames.main.Main;
import com.minegusta.mggames.rewards.ShopMenu;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class ShopTask
{
    private static int id = -1;
    public static List<Inventory> invs = Lists.newArrayList();

    public static void start()
    {
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), ShopTask::updateInvs,4,4);
    }

    public static void stop()
    {
        if(id != -1)
        {
            Bukkit.getScheduler().cancelTask(id);
        }
    }

    private static void updateInvs()
    {
        if(invs.isEmpty()) return;
        invs.stream().forEach(i ->
        {
            if(i == null || i.getContents() == null || i.getViewers().isEmpty())
            {
                invs.remove(i);
            }
            else
            {
                ShopMenu.update(i);
            }
        });
    }

    public static boolean contains(Inventory i)
    {
        return invs.contains(i);
    }

    public static void addInventory(Inventory inv)
    {
        invs.add(inv);
    }
}
