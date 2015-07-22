package com.minegusta.mggames.game;

import com.google.common.collect.Lists;
import com.minegusta.mggames.player.MGPlayer;
import org.bukkit.Location;

import java.util.List;

public class Team {

    private Location spawn = null;
    private TeamType type;
    private TeamResult result = TeamResult.LOSER;
    private List<MGPlayer> players = Lists.newArrayList();

    public Team(TeamType type)
    {
        this.type = type;
    }

    public void addPlayer(MGPlayer mgp)
    {
        players.add(mgp);
    }

    public void removePlayer(MGPlayer mgp)
    {
        if(players.contains(mgp))players.remove(mgp);
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public Location getSpawn()
    {
        return spawn;
    }

    public List<MGPlayer> getPlayers()
    {
        return players;
    }

    public void setResult(TeamResult result)
    {
        this.result = result;
    }

    public TeamResult getResult()
    {
        return result;
    }

    public TeamType getType()
    {
        return type;
    }

}
