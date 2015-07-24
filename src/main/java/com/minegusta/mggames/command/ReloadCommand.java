package com.minegusta.mggames.command;

import com.minegusta.mggames.config.ConfigManager;
import com.minegusta.mggames.game.StopReason;
import com.minegusta.mggames.kits.KitRegistry;
import com.minegusta.mggames.main.Main;
import com.minegusta.mggames.register.Register;
import com.minegusta.mggames.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!sender.isOp()) return true;

        Register.getGames().stream().forEach(game ->
        {
            game.onStop(StopReason.RELOAD);
        });

        Register.clearGames();

        KitRegistry.loadKits();

        ConfigManager.loadSessions();

        sender.sendMessage(ChatColor.GREEN + "You reloaded MGGames!");
        return true;
    }
}
