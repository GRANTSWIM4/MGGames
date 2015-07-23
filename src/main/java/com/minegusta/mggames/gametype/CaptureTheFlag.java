package com.minegusta.mggames.gametype;

import com.minegusta.mggames.config.ConfigValues;
import com.minegusta.mggames.game.*;
import com.minegusta.mggames.player.MGPlayer;
import com.minegusta.mggames.tasks.FlagTask;
import com.minegusta.mggames.util.ChatUtil;
import com.minegusta.mggames.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class CaptureTheFlag extends AbstractGame
{

    private Location teamBlueLocation = null;
    private Location teamRedLocation = null;

    private Location redWool = null;
    private Location blueWool = null;

    private int flagLives;

    private int teamRedLives = flagLives;
    private int teamBlueLives = flagLives;

    private MGPlayer redFlagCarrier = null;
    private MGPlayer blueFlagCarrier = null;

    private FlagTask redFlagTask;
    private FlagTask blueFlagTask;

    private boolean redWoolTaken = false;
    private boolean blueWoolTaken = false;

    @Override
    public AbstractGame insertData(ConfigurationSection f)
    {
        flagLives = f.getInt(ConfigValues.FLAG_LIVES.getPath(), 3);

        super.insertData(f);
        return this;
    }

    @Override
    public void onStop(StopReason reason)
    {
        super.onStop(reason);

        //Clean specific data from memory and reset edited blocks
        teamBlueLocation = null;
        teamRedLocation = null;
        redWool = null;
        blueWool = null;
        teamBlueLives = flagLives;
        teamRedLives = flagLives;
        redFlagCarrier = null;
        blueFlagCarrier = null;
        redWoolTaken = false;
        blueWoolTaken = false;
    }



    @Override
    public void onStart()
    {
        //Set specific blocks here, teleport players and do anything that has to be done
        getRedWool().getBlock().setType(Material.WOOL);
        getRedWool().getBlock().setData((byte)14);

        getBlueWool().getBlock().setType(Material.WOOL);
        getBlueWool().getBlock().setData((byte) 11);

        getTeams().stream().forEach(team ->
        {
            team.getPlayers().stream().forEach(mgp ->
            {
                mgp.getPlayer().teleport(team.getSpawn());
            });
        });
    }

    public void addScore(TeamType team, MGPlayer mgp)
    {
        if(team == TeamType.RED)
        {
            teamBlueLives--;
        }
        else if(team == TeamType.BLUE)
        {
            teamRedLives--;
        }

        checkifEnd();

        getPlayers().stream().forEach(p ->
        {
            ChatUtil.sendFormattedMessage(p.getPlayer(), mgp.getPlayer().getName() + team.getColor() + " scored a point for team " + team.name() + "!");
        });
    }

    private void checkifEnd()
    {
        if(teamRedLives <= 0 || teamBlueLives <= 0)
        {
            onStop(StopReason.WINNER);
        }
    }

    @Override
    public void onRespawn(MGPlayer mgp, String message) {

        mgp.getPlayer().teleport(mgp.getTeam().getSpawn());

        getPlayers().stream().forEach(p ->
        {
            ChatUtil.sendFormattedMessage(p.getPlayer(), message);
        });

        super.onRespawn(mgp, message);
    }

    @Override
    public void onDeath(MGPlayer mgp)
    {
        //Flag checks
        if(redFlagCarrier != null && mgp == redFlagCarrier)
        {
            returnRedWool();
        }
        else if(blueFlagCarrier != null && mgp == blueFlagCarrier)
        {
            returnBlueWool();
        }

    }

    @Override
    public void applyRewards()
    {
        //Set the winners, losers and ties.

        if(teamBlueLives > teamRedLives)
        {
            getTeam(TeamType.RED).setResult(TeamResult.LOSER);
            getTeam(TeamType.BLUE).setResult(TeamResult.WINNER);
        }
        else if(teamRedLives > teamBlueLives)
        {
            getTeam(TeamType.RED).setResult(TeamResult.WINNER);
            getTeam(TeamType.BLUE).setResult(TeamResult.LOSER);
        }
        else
        {
            getTeam(TeamType.RED).setResult(TeamResult.TIE);
            getTeam(TeamType.BLUE).setResult(TeamResult.TIE);
        }

        super.applyRewards();
    }

    @Override
    public void addTeams() {
        addTeam(TeamType.RED, new Team(TeamType.RED));
        addTeam(TeamType.BLUE, new Team(TeamType.BLUE));
    }

    @Override
    public void addLocations(ConfigurationSection f)
    {
        //add all the spawns for the teams
        ConfigurationSection section = f.getConfigurationSection(ConfigValues.WORLDS.getPath() + "." + getWorld().getName());

        teamRedLocation = LocationUtil.toLocation(section.getString(ConfigValues.TEAM_RED_SPAWN.getPath()));
        teamBlueLocation = LocationUtil.toLocation(section.getString(ConfigValues.TEAM_BLUE_SPAWN.getPath()));
        redWool = LocationUtil.toLocation(section.getString(ConfigValues.RED_WOOL.getPath()));
        blueWool = LocationUtil.toLocation(section.getString(ConfigValues.BLUE_WOOL.getPath()));

        getTeam(TeamType.BLUE).setSpawn(teamBlueLocation);
        getTeam(TeamType.RED).setSpawn(teamRedLocation);
    }

    @Override
    public void onPlayerLeave(MGPlayer mgp)
    {
        //Flag checks
        if(redFlagCarrier != null && mgp == redFlagCarrier)
        {
            returnRedWool();
        }
        else if(blueFlagCarrier != null && mgp == blueFlagCarrier)
        {
            returnBlueWool();
        }

        super.onPlayerLeave(mgp);

        if(getStage() != Stage.LOBBY && getTeams().stream().anyMatch(team -> team.getPlayers().isEmpty()))
        {
            onStop(StopReason.WINNER);
        }
    }

    @Override
    public void onPlayerJoin(MGPlayer mgp)
    {
        //Send messages and stuff
        ChatUtil.sendFormattedMessage(mgp.getPlayer(), "You joined Capture The Flag!");

        super.onPlayerJoin(mgp);
    }

                  //Game Specific Methods//
    //--------------------------------------------------//

    public Location getRedWool()
    {
        return redWool;
    }

    public Location getBlueWool()
    {
        return blueWool;
    }

    public boolean takeRedWool(MGPlayer mgp)
    {
        if(redWoolTaken || mgp.getTeam().getType() == TeamType.RED)return false;
        this.redWoolTaken = true;
        getRedWool().getBlock().setData((byte) 0);
        setRedFlagCarrier(mgp);
        return true;
    }

    public boolean takeBlueWool(MGPlayer mgp)
    {
        if(blueWoolTaken || mgp.getTeam().getType() == TeamType.BLUE)return false;
        this.blueWoolTaken = true;
        getBlueWool().getBlock().setData((byte) 0);
        setBlueFlagCarrier(mgp);
        return true;
    }

    public void returnBlueWool()
    {
        getBlueWool().getBlock().setData((byte)11);
        blueWoolTaken = false;
        blueFlagCarrier = null;
        blueFlagTask.stop();
        getPlayers().stream().forEach(p -> ChatUtil.sendFormattedMessage(p.getPlayer(), "The blue flag has been returned."));
    }

    public void returnRedWool()
    {
        getRedWool().getBlock().setData((byte)14);
        redWoolTaken = false;
        redFlagCarrier = null;
        redFlagTask.stop();
        getPlayers().stream().forEach(p -> ChatUtil.sendFormattedMessage(p.getPlayer(), "The red flag has been returned."));
    }

    public MGPlayer getRedFlagCarrier()
    {
        return redFlagCarrier;
    }

    public MGPlayer getBlueFlagCarrier()
    {
        return blueFlagCarrier;
    }

    public boolean isRedWoolTaken()
    {
        return redWoolTaken;
    }

    public boolean isBlueWoolTaken()
    {
        return blueWoolTaken;
    }

    public void setRedFlagCarrier(MGPlayer mgp)
    {
        this.redFlagCarrier = mgp;
        redFlagTask = new FlagTask(mgp, Material.REDSTONE_BLOCK);
        redFlagTask.start();
        getPlayers().stream().forEach(p -> ChatUtil.sendFormattedMessage(p.getPlayer(), "The red flag has been taken by " + mgp.getPlayer().getName() + "!"));

    }

    public void setBlueFlagCarrier(MGPlayer mgp)
    {
        this.blueFlagCarrier = mgp;
        blueFlagTask = new FlagTask(mgp, Material.LAPIS_BLOCK);
        blueFlagTask.start();
        getPlayers().stream().forEach(p -> ChatUtil.sendFormattedMessage(p.getPlayer(), "The blue flag has been taken " + mgp.getPlayer().getName() + "!"));
    }
}
