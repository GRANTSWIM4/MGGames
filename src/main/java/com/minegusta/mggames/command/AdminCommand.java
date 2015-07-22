package com.minegusta.mggames.command;

import com.google.common.collect.Lists;
import com.minegusta.mggames.config.ConfigManager;
import com.minegusta.mggames.config.ConfigValues;
import com.minegusta.mggames.main.Main;
import com.minegusta.mggames.register.Register;
import com.minegusta.mggames.util.ChatUtil;
import com.minegusta.mggames.util.LocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class AdminCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {

        if(!(s instanceof Player) || !s.isOp())return true;

        Player p = (Player) s;

        List<String> infoList = Lists.newArrayList(ChatColor.GOLD + "Please use the command like this:", "/game <gamesession> lobby", "/game <gamesession> <world> add <name>", "/game <gamesession> <world> remove <name>", " - - - - - - - -");

        for(ConfigValues v : ConfigValues.values())
        {
            infoList.add(ChatColor.GREEN + v.getPath());
        }

        if(args.length < 2)
        {
            ChatUtil.sendFormattedMessage(p, infoList);
            return true;
        }

        String session = args[0];

        Optional<String> gameName = Register.getSessionNames().stream().filter(string -> string.equalsIgnoreCase(session)).findFirst();
        if(!gameName.isPresent())
        {
            ChatUtil.sendFormattedMessage(p, "That game session does not exist in memory.");
            return true;
        }

        //Getting the config
        FileConfiguration gameConfig = ConfigManager.getGameConfig();


        if(args[1].equalsIgnoreCase("lobby"))
        {
            if(p.getWorld() != Main.getHub())
            {
                ChatUtil.sendFormattedMessage(p, "You need to use that command in the hub world!");
                return true;
            }

            gameConfig.set(gameName.get() + "." + ConfigValues.LOBBY_LOCATION.getPath(), LocationUtil.toString(p.getLocation()));

            ChatUtil.sendFormattedMessage(p, "You set the lobby location!", "Use /gamereload for changes to take effect.");

            //Saving the config
            ConfigManager.saveGameConfig(gameConfig);

            return true;
        }

        if(args.length < 4)
        {
            ChatUtil.sendFormattedMessage(p, infoList);
            return true;
        }

        String world = args[1];
        String path = args[3];

        //Editing the config

        if(!gameConfig.isSet(gameName.get() + "." + ConfigValues.WORLDS.getPath() + "." + world))
        {
            ChatUtil.sendFormattedMessage(p, "That world does not exist!");
            return true;
        }

        String pathToWorld = gameName.get() + "." + ConfigValues.WORLDS.getPath() + "." + world;

        if(!p.getWorld().getName().equals(world))
        {
            ChatUtil.sendFormattedMessage(p, "You have to be in that world to edit the spawnpoints!");
            return true;
        }

        String saved = LocationUtil.toString(p.getLocation());

        if(args[2].equalsIgnoreCase("add"))
        {
            gameConfig.set(pathToWorld + "." + path, saved);
            ChatUtil.sendFormattedMessage(p, "You added a location to the config!", "Use /gamereload for changes to take effect.");
        }
        else if(args[2].equalsIgnoreCase("remove"))
        {
            gameConfig.set(pathToWorld + "." + path, null);
            ChatUtil.sendFormattedMessage(p, "You removed a location from the config!", "Use /gamereload for changes to take effect.");
        }
        else
        {
            ChatUtil.sendFormattedMessage(p, infoList);
            return true;
        }

        //Saving the config
        ConfigManager.saveGameConfig(gameConfig);

        return true;
    }
}
