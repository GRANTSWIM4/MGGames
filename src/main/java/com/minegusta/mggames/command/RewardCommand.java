package com.minegusta.mggames.command;

import com.minegusta.mggames.rewards.RewardMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RewardCommand implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!(sender instanceof Player))return false;

        Player p = (Player) sender;

        RewardMenu.openRewardMenu(p);

        return true;
    }
}
