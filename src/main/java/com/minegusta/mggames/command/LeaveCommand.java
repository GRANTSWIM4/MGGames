package com.minegusta.mggames.command;

import com.minegusta.mggames.player.MGPlayer;
import com.minegusta.mggames.register.Register;
import com.minegusta.mggames.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player))return false;

        Player p = (Player) sender;
        MGPlayer mgp = Register.getPlayer(p);

        if(mgp.getSession() != null)
        {
            mgp.getSession().onPlayerLeave(mgp);
        }
        else
        {
            ChatUtil.sendFormattedMessage(p, "You are not part of a game.");
        }
        return true;
    }
}
