package com.minegusta.mggames.tasks;

import com.minegusta.mggames.game.Stage;
import com.minegusta.mggames.game.StopReason;
import com.minegusta.mggames.main.Main;
import com.minegusta.mggames.register.Register;
import org.bukkit.Bukkit;

public class ClockTask
{
    private static int id = -1;

    public static void start()
    {
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), ()->
                Register.getGames().stream().forEach(game ->
                {
                   if(game.getStage() == Stage.PLAYING)
                   {
                       game.addtime();
                       if(game.getMaxPlayTime() > 0 && game.getTime() > game.getMaxPlayTime())
                       {
                           game.onStop(StopReason.TIME_UP);
                       }
                   }
                }),20 * 60, 20 * 60);
    }

    public static void stop()
    {
        if(id != -1)
        {
            Bukkit.getScheduler().cancelTask(id);
        }
    }
}
