package com.minegusta.mggames.command;

import com.minegusta.mggames.game.AbstractGame;
import com.minegusta.mggames.game.Stage;
import com.minegusta.mggames.player.MGPlayer;
import com.minegusta.mggames.register.Register;
import com.minegusta.mggames.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class JoinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) return false;

        Player p = (Player) sender;

        if(args.length == 0)
        {
            ChatUtil.sendFormattedMessage(p, ChatColor.YELLOW + "Use the command like this:", "/Join <Name>");
            return true;
        }

        MGPlayer mgp = Register.getPlayer(p);

        if(mgp.getSession() == null)
        {
            String name = args[0];

            Optional<String> gotten = Register.getSessionNames().stream().filter(s -> s.equalsIgnoreCase(name)).findFirst();

            if(gotten.isPresent())
            {
                AbstractGame game = Register.getGame(gotten.get());

                if(game.getStage() != Stage.LOBBY)
                {
                    ChatUtil.sendFormattedMessage(p, "That game already started.");
                }
                else if(game.getPlayers().size() >= game.getMaxPlayers())
                {
                    ChatUtil.sendFormattedMessage(p, "That game is full!");
                }
                else
                {
                    game.onPlayerJoin(mgp);
                }
            }
            else
            {
                ChatUtil.sendFormattedMessage(p, "That game could not be found!");
            }

        }
        else
        {
            ChatUtil.sendFormattedMessage(p, "You are already part of another game!", "Use /Leave to leave it.");
        }
        return true;
    }
}
