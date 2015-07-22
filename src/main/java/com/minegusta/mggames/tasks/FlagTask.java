package com.minegusta.mggames.tasks;

import com.minegusta.mggames.main.Main;
import com.minegusta.mggames.player.MGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;

public class FlagTask
{
    private int id = -1;
    private MGPlayer mgp;
    private Material block;

    public FlagTask(MGPlayer mgp, Material block)
    {
        this.mgp = mgp;
        this.block = block;
    }

    public void start()
    {
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), ()->
        {
            if(mgp == null) stop();
            mgp.getPlayer().getWorld().spigot().playEffect(mgp.getPlayer().getLocation(), Effect.TILE_BREAK, block.getId(), 0, 1, 1, 1, 1/5, 20, 40);
        },20,20);
    }

    public void stop()
    {
        if(id != -1)
        {
            Bukkit.getScheduler().cancelTask(id);
        }
    }

}
