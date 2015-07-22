package com.minegusta.mggames.command;

import com.minegusta.mggames.player.MGPlayer;
import com.minegusta.mggames.register.Register;
import com.minegusta.mggames.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class TicketCommand implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player && (!sender.isOp() ||args.length == 0)) {
            Player p = (Player) sender;
            MGPlayer mgp = Register.getPlayer(p);

            ChatUtil.sendFormattedMessage(p, ChatColor.LIGHT_PURPLE + "You have " + ChatColor.DARK_PURPLE + mgp.getTickets() + ChatColor.LIGHT_PURPLE + " tickets.", "Spend your tickets at the ticket store in the mini-game hub!");
            return true;
        }
        else if((sender instanceof ConsoleCommandSender || sender.isOp()) && args.length > 2)
        {
            if(args[0].equalsIgnoreCase("add"))
            {
                int amount;
                Player p;
                try {
                    amount = Integer.parseInt(args[2]);
                    p = Bukkit.getPlayer(args[1]);
                } catch (Exception ignored)
                {
                    sender.sendMessage(ChatColor.RED + "Invalid player or amount.");
                    sender.sendMessage(ChatColor.RED + "Usage: /tickets add <name> <amount>");
                    return true;
                }
                MGPlayer mgp = Register.getPlayer(p);
                mgp.addTickets(amount);
                return true;
            }
            sender.sendMessage(ChatColor.RED + "Usage: /tickets add <name> <amount>");
            return true;
        }
        return true;
    }
}
