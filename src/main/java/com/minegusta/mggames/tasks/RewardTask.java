package com.minegusta.mggames.tasks;

import com.minegusta.mggames.main.Main;
import com.minegusta.mggames.register.Register;
import com.minegusta.mggames.rewards.UnlockableType;
import org.bukkit.Bukkit;

public class RewardTask
{
    private static int id = -1;

    public static void start()
    {
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), RewardTask::boost,20, 20);
    }

    public static void stop()
    {
        if(id != -1)
        {
            Bukkit.getScheduler().cancelTask(id);
        }
    }

    private static void boost()
    {
        Register.getPlayers().stream().forEach(mgp ->
        {
            mgp.getActiveUnlockables().stream().forEach(u ->
            {
                if(u.getType() == UnlockableType.TIMED) u.apply(mgp.getPlayer());
            });
        });
    }
}
