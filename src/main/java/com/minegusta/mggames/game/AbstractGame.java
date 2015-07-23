package com.minegusta.mggames.game;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.minegusta.mggames.config.ConfigValues;
import com.minegusta.mggames.kits.KitRegistry;
import com.minegusta.mggames.main.Main;
import com.minegusta.mggames.player.MGPlayer;
import com.minegusta.mggames.tasks.Timer;
import com.minegusta.mggames.util.ChatUtil;
import com.minegusta.mggames.util.LocationUtil;
import com.minegusta.mggames.util.ScoreboardUtil;
import com.minegusta.mggames.world.WorldManager;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractGame {
    private Stage stage = Stage.LOBBY;
    private ConcurrentMap<String, MGPlayer> players = Maps.newConcurrentMap();
    private GameTypes type;
    private List<String> voted = Lists.newArrayList();
    private String world = null;
    private String name = "Game";
    private int currentPlayTime;
    private boolean buildBlocks;
    private boolean breakBlocks;
    private int minPlayers;
    private boolean respawnWithKit;
    private boolean forceKit;
    private String defaultKit;
    private List<String> winnerCommands = Lists.newArrayList();
    private List<String> loserCommands = Lists.newArrayList();
    private List<String> tieCommands = Lists.newArrayList();
    private int maxPlayers;
    private boolean starting = false;
    private ConfigurationSection f;
    private int graceTime;
    private int maxtime;

    private ConcurrentMap<TeamType,Team> teams = Maps.newConcurrentMap();

    private Location lobby;

    private ConcurrentMap<String, Integer> worlds = Maps.newConcurrentMap();

    //-------------------------------------------------------------------

    public AbstractGame()
    {

    }

    public AbstractGame insertData(ConfigurationSection f)
    {
        this.f = f;

        this.type = GameTypes.valueOf(f.getString(ConfigValues.TYPE.getPath(), null));

        f.getConfigurationSection(ConfigValues.WORLDS.getPath()).getKeys(false).stream().filter(s -> WorldManager.worldExists(s) && !Bukkit.getWorlds().stream().anyMatch(w -> w.getName().equals(s))).forEach(s -> worlds.put(s, 0));

        if(f.isSet(ConfigValues.WINNER_COMMANDS.getPath()))this.winnerCommands = f.getStringList(ConfigValues.WINNER_COMMANDS.getPath());
        if(f.isSet(ConfigValues.LOSER_COMMANDS.getPath()))this.loserCommands = f.getStringList(ConfigValues.LOSER_COMMANDS.getPath());
        if(f.isSet(ConfigValues.TIE_COMMANDS.getPath()))this.tieCommands = f.getStringList(ConfigValues.TIE_COMMANDS.getPath());

        respawnWithKit = f.getBoolean(ConfigValues.RESPAWN_WITH_KIT.getPath(), false);

        forceKit = f.getBoolean(ConfigValues.FORCE_KIT.getPath(), false);

        defaultKit = f.getString(ConfigValues.DEFAULT_KIT.getPath(), "Warrior");

        maxPlayers = f.getInt(ConfigValues.MAX_PLAYERS.getPath(), -1);

        minPlayers = f.getInt(ConfigValues.MIN_PLAYERS.getPath(), -1);

        graceTime = f.getInt(ConfigValues.GRACE_TIME.getPath(), 10);

        maxtime = f.getInt(ConfigValues.MAX_PLAYTIME.getPath(), -1);

        buildBlocks = f.getBoolean(ConfigValues.ALLOW_BUILD.getPath(), false);

        breakBlocks = f.getBoolean(ConfigValues.ALLOW_BREAK.getPath(), false);

        name = f.getString(ConfigValues.NAME.getPath(), "Game");

        lobby = LocationUtil.toLocation(f.getString(ConfigValues.LOBBY_LOCATION.getPath(), null));
        if(lobby == null) lobby = Main.getHub().getSpawnLocation();

        addTeams();

        return this;
    }

    //-------------------------------------------------------------------

    public abstract void onStart();

    public void onRespawn(MGPlayer mgp, String message)
    {
        mgp.getPlayer().setHealth(mgp.getPlayer().getMaxHealth());
        mgp.getPlayer().setFoodLevel(20);
        if(respawnWithKit() && mgp.getKit() != null)
        {
            mgp.getKit().apply(mgp.getPlayer());
        }
    }

    public abstract void onDeath(MGPlayer mgp);
    
    public void applyRewards()
    {
        getTeams().stream().forEach(team ->
                rewardPlayers(team, team.getResult()));
    }

    public String getDefaultKit()
    {
        return defaultKit;
    }

    public boolean forceKit()
    {
        return forceKit;
    }


    public abstract void addTeams();

    public abstract void addLocations(ConfigurationSection f);

    public void setTeam(MGPlayer mgp)
    {
        Team joined = null;
        int leastUsers = 0;

        for(Team team : teams.values())
        {
            if(team.getType() == TeamType.SPECTATOR)continue;

            int size = team.getPlayers().size();
            if(size <= leastUsers)
            {
                joined = team;
                leastUsers = size;
            }
        }
        setTeam(mgp, joined);
    }

    public boolean respawnWithKit()
    {
        return respawnWithKit;
    }

    private void rewardWinners(List<MGPlayer> winners)
    {
        winnerCommands.stream().forEach(s ->
                winners.stream().forEach(p ->
                {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%name%", p.getPlayer().getName()));
                    ChatUtil.sendFormattedMessage(p.getPlayer(), "You have been rewarded for playing and winning the game!");
                }));
    }

    private void rewardTie(List<MGPlayer> ties)
    {
        tieCommands.stream().forEach(s ->
                ties.stream().forEach(p ->
                {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%name%", p.getPlayer().getName()));
                    ChatUtil.sendFormattedMessage(p.getPlayer(), "You have been rewarded for playing!");
                }));
    }

    private void rewardLosers(List<MGPlayer> losers)
    {
        loserCommands.stream().forEach(s ->
                losers.stream().forEach(p ->
                {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%name%", p.getPlayer().getName()));
                    ChatUtil.sendFormattedMessage(p.getPlayer(), "You have been rewarded for playing, even though you lost!");
                }));
    }

    public void addTeam(TeamType type, Team team)
    {
        teams.put(type, team);
    }
    
    public Team getTeam(TeamType type)
    {
        return teams.get(type);
    }
    
    private void rewardPlayers(Team team, TeamResult result)
    {
        if(result == TeamResult.TIE)
        {
            rewardTie(team.getPlayers());
        }
        else if(result == TeamResult.WINNER)
        {
            rewardWinners(team.getPlayers());
        }
        else if(result == TeamResult.LOSER)
        {
            rewardLosers(team.getPlayers());
        }
    }


    public void setTeam(MGPlayer mgp, Team team)
    {
        ChatUtil.sendFormattedMessage(mgp.getPlayer(), "You joined team " + team.getType().name() + ".");
        removeFromTeam(mgp);
        mgp.setTeam(team);
        team.addPlayer(mgp);
        ScoreboardUtil.addScoreBoard(mgp.getPlayer(), team.getType());

        if(team.getType() == TeamType.SPECTATOR)
        {
            mgp.getPlayer().setGameMode(GameMode.SPECTATOR);
            ChatUtil.sendFormattedMessage(mgp.getPlayer(), "You are now spectating!", "Use " + ChatColor.YELLOW + "/Leave " + ChatColor.GRAY + "to leave.", "Leaving means you will not get a reward for playing.");
        }
    }

    public int getGraceTime()
    {
        return graceTime;
    }

    public void removeFromTeam(MGPlayer mgp)
    {
        if(mgp.getTeam() == null)return;
        mgp.getTeam().removePlayer(mgp);
        mgp.setTeam(null);
        mgp.getPlayer().setGameMode(GameMode.SURVIVAL);
        ScoreboardUtil.setHubBoard(mgp.getPlayer());

        mgp.getPlayer().getActivePotionEffects().stream().forEach(e ->
        {
            mgp.getPlayer().removePotionEffect(e.getType());
        });
    }

    public Collection<Team> getTeams()
    {
        return teams.values();
    }

    public void onPlayerJoin(MGPlayer mgp)
    {
        players.put(mgp.getUniqueIdAsString(), mgp);
        mgp.purgeStats();
        mgp.clearInventory();
        setTeam(mgp);
        mgp.setSession(this);

        mgp.getPlayer().setHealth(mgp.getPlayer().getMaxHealth());
        mgp.getPlayer().setFoodLevel(20);

        if(forceKit && KitRegistry.exists(getDefaultKit()))
        {
            mgp.setKit(KitRegistry.getKit(getDefaultKit()));
        }

        mgp.getPlayer().teleport(lobby);

        if(getPlayers().size() >= getMinPlayers() && !starting)
        {
            Timer.startLobbyTimer(this);
            starting = true;
        }

        List<String> votes = Lists.newArrayList(ChatColor.GREEN + "[World Votes]");
        worlds.keySet().stream().forEach(s ->
        {
            String add = ChatColor.BLUE + s + ": " + ChatColor.AQUA + worlds.get(s);
            votes.add(add);
        });

        votes.add(ChatColor.GRAY + "/vote <map>");
        ChatUtil.sendFormattedMessage(mgp.getPlayer(), votes);
    }

    public void cancelStarting()
    {
        this.starting = false;
    }

    public boolean allowBreaking()
    {
        return breakBlocks;
    }

    public boolean allowPlacing()
    {
        return buildBlocks;
    }

    public void onPlayerLeave(MGPlayer mgp)
    {
        if(players.containsKey(mgp.getUniqueIdAsString()))
        {
            ChatUtil.sendFormattedMessage(mgp.getPlayer(), "You have left " + name + ".");
            players.remove(mgp.getUniqueIdAsString());
        }
        removeFromTeam(mgp);
        mgp.purgeStats();
        mgp.clearInventory();
        mgp.getPlayer().setGameMode(GameMode.SURVIVAL);
        mgp.getPlayer().teleport(Main.getHub().getSpawnLocation());
        mgp.giveWatch();
    }


    public void addtime()
    {
        currentPlayTime++;
    }

    public int getTime()
    {
        return currentPlayTime;
    }

    public int getMinPlayers()
    {
        return minPlayers;
    }

    public int getMaxPlayers()
    {
        return maxPlayers;
    }

    public int getMaxPlayTime()
    {
        return maxtime;
    }

    public void loadWorld()
    {
        if(world == null || !WorldManager.worldExists(world) || WorldManager.isLoaded(world))
        {
            onStop(StopReason.WORLD_ERROR);
            return;
        }
        WorldManager.loadWorld(world, false);
    }

    public Location getLobby()
    {
        return lobby;
    }

    public World getWorld()
    {
        return Bukkit.getWorld(world);
    }

    public void onStop(StopReason reason)
    {
        applyRewards();

        //Clear the player's data
        players.values().stream().forEach(mgp ->
        {
            mgp.getPlayer().setGameMode(GameMode.SURVIVAL);
            mgp.getPlayer().teleport(Main.getHub().getSpawnLocation());
            mgp.purgeStats();
        });

        //clear the team data
        getTeams().stream().forEach(team ->
        {
            team.setResult(TeamResult.LOSER);
            team.getPlayers().clear();
            team.setSpawn(null);
        });

        if(world != null)
        {
            if(getWorld() != null)
            {
                for(Player p : getWorld().getPlayers())
                {
                    p.teleport(Main.getHub().getSpawnLocation());
                }
            }
            //unload the played world
            WorldManager.unLoadWorld(world, false);
        }

        for(Player p : Main.getHub().getPlayers())
        {
            ChatUtil.sendFormattedMessage(p, ChatColor.YELLOW + name + " has ended!");
        }

        //clear the data
        voted.clear();
        players.clear();
        cancelStarting();
        currentPlayTime = 0;

        //Clear world votes
        worlds.keySet().stream().forEach(i -> worlds.put(i, 0));
        getTeams().stream().forEach(t -> t.getPlayers().clear());

        //Reset the stage
        stage = Stage.LOBBY;

        //Clear the map data
        world = null;
    }

    public Stage getStage()
    {
        return stage;
    }

    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    public GameTypes getGameType()
    {
        return type;
    }

    public Collection<MGPlayer> getPlayers()
    {
        return players.values();
    }

    public Collection<String> getWorlds()
    {
        return worlds.keySet();
    }

    public void pickWorld()
    {
        int max = 0;
        String chosen = "";

        for(String mapName : worlds.keySet())
        {
            if(worlds.get(mapName) >= max)
            {
                max = worlds.get(mapName);
                chosen = mapName;
            }
        }
        world = chosen;

        loadWorld();

        addLocations(f);
    }

    public void addvote(Player p, String world)
    {
        if(stage != Stage.LOBBY)
        {
            ChatUtil.sendFormattedMessage(p, "You can only vote in the lobby!");
            return;
        }
        if(!voted.contains(p.getUniqueId().toString()))
        {
            if(worlds.keySet().stream().anyMatch(s -> s.equalsIgnoreCase(world)))
            {
                voted.add(p.getUniqueId().toString());

                String chosen = worlds.keySet().stream().filter(w -> w.equalsIgnoreCase(world)).findFirst().get();

                worlds.put(chosen, worlds.get(chosen) + 1);

                List<String> votes = Lists.newArrayList(ChatColor.GREEN + "[World Votes]");
                worlds.keySet().stream().forEach(s ->
                {
                    String add = ChatColor.BLUE + s + ": " + ChatColor.LIGHT_PURPLE + worlds.get(s);
                    votes.add(add);
                });

                ChatUtil.sendFormattedMessage(p, ChatColor.GRAY + "You voted for " + world.toLowerCase() + ".");
                for(MGPlayer mgp : getPlayers())
                {
                    ChatUtil.sendFormattedMessage(mgp.getPlayer(), votes);
                }
            }
            else
            {
                ChatUtil.sendFormattedMessage(p, ChatColor.GRAY + "That map does not exist!");
            }
            return;
        }
        ChatUtil.sendFormattedMessage(p, ChatColor.GRAY + "You have already voted!");
    }
}
