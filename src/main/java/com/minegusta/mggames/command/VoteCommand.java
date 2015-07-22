package com.minegusta.mggames.command;

import com.minegusta.mggames.player.MGPlayer;
import com.minegusta.mggames.register.Register;
import com.minegusta.mggames.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoteCommand implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args)
    {
        if(!(s instanceof Player))return false;

        Player p = (Player) s;

        MGPlayer mgp = Register.getPlayer(p);

        if(args.length == 0)
        {
            ChatUtil.sendFormattedMessage(p, "Use the command like this: ", "/Vote <MapeName>");
            return true;
        }

        if(mgp.getSession() != null)
        {
            mgp.getSession().addvote(p, args[0]);
        }
        return true;
    }
}
