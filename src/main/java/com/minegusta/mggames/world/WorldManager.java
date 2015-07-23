package com.minegusta.mggames.world;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

import java.io.File;

public class WorldManager
{
    public static boolean loadWorld(String world, boolean save)
    {
        if(worldExists(world))
        {
            Bukkit.getServer().createWorld(new WorldCreator(world)).setAutoSave(save);
            return true;
        }
        return false;
    }

    public static boolean isLoaded(String world)
    {
        return Bukkit.getWorlds().stream().anyMatch(w -> w.getName().equals(world));
    }

    public static boolean unLoadWorld(String world, boolean save)
    {
        return Bukkit.unloadWorld(world, save);
    }

    public static boolean worldExists(String world)
    {
        return new File("./" + world).exists();
    }
}