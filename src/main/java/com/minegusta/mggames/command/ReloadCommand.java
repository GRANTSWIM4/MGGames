package com.minegusta.mggames.command;

import com.minegusta.mggames.config.ConfigManager;
import com.minegusta.mggames.game.StopReason;
import com.minegusta.mggames.kits.KitRegistry;
import com.minegusta.mggames.main.Main;
import com.minegusta.mggames.register.Register;
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

        ConfigManager.loadSessions();
        KitRegistry.loadKits();

        return true;
    }
}
