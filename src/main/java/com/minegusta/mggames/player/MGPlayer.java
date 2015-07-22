package com.minegusta.mggames.player;

import com.minegusta.mggames.game.AbstractGame;
import com.minegusta.mggames.game.Team;
import com.minegusta.mggames.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MGPlayer {

    private int kills = 0;
    private int deaths = 0;
    private AbstractGame session;
    private Team team;
    private Kit kit;
    private String uuid;

    public MGPlayer(Player p) {
        this.session = null;
        this.team = null;
        this.kit = null;
        this.uuid = p.getUniqueId().toString();
    }

    public AbstractGame getSession() {
        return session;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(UUID.fromString(uuid));
    }

    public Team getTeam() {
        return team;
    }

    public int getKills() {
        return kills;
    }

    public void setKit(Kit kit)
    {
        this.kit = kit;
        kit.apply(getPlayer());
    }

    public void setSession(AbstractGame game)
    {
        this.session = game;
    }

    public void setTeam(Team team)
    {
        this.team = team;
    }

    public String getUniqueIdAsString()
    {
        return uuid;
    }

    public int getDeaths()
    {
        return deaths;
    }

    public void setDeaths(int deaths)
    {
        this.deaths = deaths;
    }

    public void setKills(int kills)
    {
        this.kills = kills;
    }

    public Kit getKit()
    {
        return kit;
    }

    public void purgeStats()
    {
        this.session = null;
        if(team != null && team.getPlayers().contains(this)) team.getPlayers().remove(this);
        this.team = null;
        this.deaths = 0;
        this.kit = null;
        this.kills = 0;

        getPlayer().getActivePotionEffects().stream().forEach(e ->
        {
            getPlayer().removePotionEffect(e.getType());
        });
    }
}
