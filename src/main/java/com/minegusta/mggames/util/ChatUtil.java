package com.minegusta.mggames.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class ChatUtil
{
    public static void sendFormattedMessage(Player p, String... s)
    {
        for(String string : s)
        {
            p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "DG" + ChatColor.DARK_GRAY + "-" + ChatColor.DARK_AQUA + "MG" + ChatColor.DARK_GRAY + "]" + ChatColor.YELLOW +  string);
        }
    }

    public static void sendFormattedMessage(Player p, List<String> s)
    {
        for(String string : s)
        {
            p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "DG" + ChatColor.DARK_GRAY + "-" + ChatColor.DARK_AQUA + "MG" + ChatColor.DARK_GRAY + "]" + ChatColor.YELLOW +  string);
        }
    }

    public static void sendGameMessage(Player p, String... messages)
    {
        for(String s : messages)
        {
            p.sendMessage(ChatColor.LIGHT_PURPLE + "[Info] " + ChatColor.YELLOW + s);
        }
    }
}
