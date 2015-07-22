package com.minegusta.mggames.tasks;

import com.minegusta.mggames.game.AbstractGame;
import com.minegusta.mggames.game.Stage;
import com.minegusta.mggames.main.Main;
import com.minegusta.mggames.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Timer {

    public static void startLobbyTimer(AbstractGame game)
    {
        Stage stage = game.getStage();
        if(stage == Stage.LOBBY)
        {
            for(int i = 0; i <= 30; i++)
            {
                final int k = i;
                if(i % 5 == 0)
                {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), ()->
                    {
                        int left  = 30 - k;
                       game.getPlayers().stream().forEach(mgp -> ChatUtil.sendFormattedMessage(mgp.getPlayer(), left + " Seconds till game start!"));
                    },20*i);

                    //Start the game
                    if(k == 30)
                    {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), ()->
                        {
                            int players = game.getPlayers().size();
                            int minPlayers = game.getMinPlayers();

                            if(players >= minPlayers)
                            {
                                game.setStage(Stage.GRACE);
                                game.pickWorld();
                                game.getPlayers().stream().forEach(mgp -> ChatUtil.sendFormattedMessage(mgp.getPlayer(), "Loading world and starting game."));
                                game.loadWorld();
                                game.onStart();
                                startGraceTimer(game);
                            }
                            else
                            {
                                game.cancelStarting();
                                game.getPlayers().stream().forEach(mgp -> ChatUtil.sendFormattedMessage(mgp.getPlayer(), "Not enough players. Waiting for more to join."));
                            }
                        },20*i);
                    }
                }
            }
        }
    }

    public static void startGraceTimer(AbstractGame game)
    {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), ()->
        {
            game.getPlayers().stream().forEach(mgp -> ChatUtil.sendFormattedMessage(mgp.getPlayer(), ChatColor.DARK_RED + "The game has started! You can now receive damage"));
            game.setStage(Stage.PLAYING);
        },20 * game.getGraceTime());
    }
}
