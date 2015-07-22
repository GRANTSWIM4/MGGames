package com.minegusta.mggames.main;

import com.minegusta.mggames.command.*;
import com.minegusta.mggames.config.ConfigManager;
import com.minegusta.mggames.game.StopReason;
import com.minegusta.mggames.kits.KitRegistry;
import com.minegusta.mggames.register.Register;
import com.minegusta.mggames.tasks.ClockTask;
import com.minegusta.mggames.tasks.TeamPotionTask;
import com.minegusta.mggames.util.ScoreboardUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Plugin PLUGIN;
    private static World hub;

    @Override
    public void onEnable()
    {
        PLUGIN = this;
        ConfigManager.loadDefaultConfig();

        //Getting the hub world.
        hub = Bukkit.getWorld(ConfigManager.getDefaultConfig().getString("hub-world"));

        //Commands
        getCommand("vote").setExecutor(new VoteCommand());
        getCommand("game").setExecutor(new AdminCommand());
        getCommand("join").setExecutor(new JoinCommand());
        getCommand("leave").setExecutor(new LeaveCommand());
        getCommand("gamereload").setExecutor(new ReloadCommand());
        getCommand("kit").setExecutor(new KitCommand());
        getCommand("world").setExecutor(new LoadWorldCommand());

        //Listeners
        for(Listener l : Listener.values())
        {
            Bukkit.getPluginManager().registerEvents(l.getListener(), this);
        }

        //Loading all the game sessions
        ConfigManager.loadSessions();

        //Load all kits
        KitRegistry.loadKits();

        //Tasks
        ClockTask.start();
        TeamPotionTask.start();

        //Set the scoreboard
        ScoreboardUtil.setBoard();

    }

    @Override
    public void onDisable()
    {

        //Stop all games
        Register.getGames().stream().forEach(game ->
        {
            game.onStop(StopReason.RELOAD);
        });

        //Stop tasks
        ClockTask.stop();
        TeamPotionTask.stop();

    }

    public static Plugin getPlugin()
    {
        return PLUGIN;
    }

    public static World getHub()
    {
        return hub;
    }
}
