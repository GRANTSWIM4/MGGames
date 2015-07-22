package com.minegusta.mggames.tasks;

import com.minegusta.mggames.main.Main;
import com.minegusta.mggames.register.Register;
import org.bukkit.Bukkit;

public class TeamPotionTask
{
    private static int id = -1;

    public static void start() {
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () ->
                Register.getPlayers().stream().forEach(p ->
                {
                    if(p.getKit() != null)
                    {
                        p.getKit().applyEffects(p.getPlayer());
                    }
                }), 20 * 6, 20 * 6);
    }


    public static void stop()
    {
        if(id != -1)
        {
            Bukkit.getScheduler().cancelTask(id);
        }
    }
}
