package com.minegusta.mggames.tasks;

import com.minegusta.mggames.main.Main;
import com.minegusta.mggames.player.MGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
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
            Location l = mgp.getPlayer().getLocation();
            l.add(0,2,0);
            mgp.getPlayer().getWorld().spigot().playEffect(mgp.getPlayer().getLocation(), Effect.TILE_BREAK, block.getId(), 0, 0.5F, 0.5F, 0.5F, 1/10, 15, 40);
        },10,10);
    }

    public void stop()
    {
        if(id != -1)
        {
            Bukkit.getScheduler().cancelTask(id);
        }
    }

}
