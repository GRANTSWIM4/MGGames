package com.minegusta.mggames.game;

import org.bukkit.ChatColor;

public enum TeamType
{
    FFA(ChatColor.GRAY),
    RED(ChatColor.RED),
    BLUE(ChatColor.BLUE),
    SPECTATOR(ChatColor.GOLD);

    private ChatColor color;

    TeamType(ChatColor color)
    {
        this.color = color;
    }

    public ChatColor getColor()
    {
        return color;
    }
}
