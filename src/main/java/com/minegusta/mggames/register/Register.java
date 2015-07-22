package com.minegusta.mggames.register;

import com.google.common.collect.Maps;
import com.minegusta.mggames.config.ConfigManager;
import com.minegusta.mggames.game.AbstractGame;
import com.minegusta.mggames.player.MGPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

public class Register
{
    public static ConcurrentMap<String,AbstractGame> games = Maps.newConcurrentMap();
    public static ConcurrentMap<String, MGPlayer> players = Maps.newConcurrentMap();

    public static void registerGame(String name, AbstractGame game)
    {
        games.put(name, game);
    }

    public static Collection<AbstractGame> getGames()
    {
        return games.values();
    }

    public static AbstractGame getGame(String name)
    {
        return games.get(name);
    }

    public static void registerPlayer(Player p)
    {
        players.put(p.getUniqueId().toString(), new MGPlayer(p, ConfigManager.loadPlayerFile(p)));
    }

    public static void removePlayer(String uuid)
    {
        if(players.containsKey(uuid))players.remove(uuid);
    }

    public static MGPlayer getPlayer(String uuid)
    {
        return players.get(uuid);
    }

    public static MGPlayer getPlayer(Player p)
    {
        return getPlayer(p.getUniqueId().toString());
    }

    public static Collection<MGPlayer> getPlayers()
    {
        return players.values();
    }

    public static Collection<String> getSessionNames()
    {
        return games.keySet();
    }
}
