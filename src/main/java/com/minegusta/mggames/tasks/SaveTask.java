package com.minegusta.mggames.tasks;

import com.minegusta.mggames.main.Main;
import com.minegusta.mggames.player.MGPlayer;
import com.minegusta.mggames.register.Register;
import org.bukkit.Bukkit;

public class SaveTask {
    private static int id = -1;

    public static void stop()
    {
        if(id != -1)
        {
            Bukkit.getScheduler().cancelTask(id);
        }
    }

    public static void start()
    {
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), SaveTask::save,20*60, 20*60);
    }

    public static void save()
    {
        Register.getPlayers().stream().forEach(MGPlayer::saveConfig);
    }
}
