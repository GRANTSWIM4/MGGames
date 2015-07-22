package com.minegusta.mggames.command;

import com.minegusta.mggames.main.Main;
import com.minegusta.mggames.player.MGPlayer;
import com.minegusta.mggames.register.Register;
import com.minegusta.mggames.util.ChatUtil;
import com.minegusta.mggames.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoadWorldCommand implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player) || !sender.isOp())return true;

        Player p = (Player) sender;

        if(args.length < 1)
        {
            ChatUtil.sendFormattedMessage(p, "Use the command like this:", "/world <load/unload/teleport> <world>", "/world list");
            return true;
        }

        if(args[0].equalsIgnoreCase("list"))
        {
            ChatUtil.sendFormattedMessage(p, ChatColor.YELLOW + " - - -Loaded worlds:- - - ");
            Bukkit.getWorlds().stream().forEach(world ->
            {
                p.sendMessage(ChatColor.GRAY + world.getName());
            });
            return true;
        }

        if(args.length < 2)
        {
            ChatUtil.sendFormattedMessage(p, "Use the command like this:", "/world <load/unload/teleport> <world>", "/world list");
            return true;
        }

        String worldName = args[1];

        if(!WorldManager.worldExists(worldName))
        {
            ChatUtil.sendFormattedMessage(p, "That world does not exist.", "Note: world names are case sensitive.");
            return true;
        }

        if(worldName.equalsIgnoreCase(Main.getHub().getName()) && !args[0].equalsIgnoreCase("teleport"))
        {
            ChatUtil.sendFormattedMessage(p, "You cannot unload the hub.");
            return true;
        }

        String l = args[0];

        if(l.equalsIgnoreCase("load"))
        {
            if(Bukkit.getWorlds().stream().anyMatch(w -> w.getName().equals(worldName)))
            {
                ChatUtil.sendFormattedMessage(p, "That world is already loaded.", "Use /world teleport <name> to teleport there.");
                return true;
            }

            WorldManager.loadWorld(worldName);
            ChatUtil.sendFormattedMessage(p, "You loaded the world " + worldName + ".");
            return true;

        }
        else if(l.equalsIgnoreCase("unload"))
        {
            if(!Bukkit.getWorlds().stream().anyMatch(w -> w.getName().equals(worldName)))
            {
                ChatUtil.sendFormattedMessage(p, "That world is not loaded.", "Use /world load <name> to load it.");
                return true;
            }
            World unloading = Bukkit.getWorld(worldName);
            unloading.getPlayers().stream().forEach(player ->
            {
                MGPlayer mgp = Register.getPlayer(player);
                player.teleport(Main.getHub().getSpawnLocation());
                if (mgp.getSession() != null) {
                    mgp.getSession().onPlayerLeave(mgp);
                }
            });

            Bukkit.unloadWorld(unloading, true);
            ChatUtil.sendFormattedMessage(p, "You unloaded world " + worldName + ".");
            return true;
        }
        else if(l.equalsIgnoreCase("teleport"))
        {
            if(Bukkit.getWorlds().stream().anyMatch(w -> w.getName().equals(worldName)))
            {
                World world = Bukkit.getWorld(worldName);
                p.teleport(world.getSpawnLocation());
                ChatUtil.sendFormattedMessage(p, "You teleported to another world!");
                return true;
            }
        }

        ChatUtil.sendFormattedMessage(p, "Use the command like this:", "/world <load/unload> <world>");
        return true;
    }
}
