package com.minegusta.mggames.player;

import com.google.common.collect.Lists;
import com.minegusta.mggames.config.ConfigManager;
import com.minegusta.mggames.config.ConfigValues;
import com.minegusta.mggames.game.AbstractGame;
import com.minegusta.mggames.game.Team;
import com.minegusta.mggames.kits.Kit;
import com.minegusta.mggames.rewards.Unlockable;
import com.minegusta.mggames.util.ChatUtil;
import com.minegusta.mggames.util.ScoreboardUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class MGPlayer {

    private static final ItemStack watch = new ItemStack(Material.WATCH, 1)
    {
        {
            ItemMeta meta = getItemMeta();
            meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Teleport Interface");
            meta.setLore(Lists.newArrayList(ChatColor.LIGHT_PURPLE + "Use this to navigate!"));

            setItemMeta(meta);
        }
    };

    private int kills = 0;
    private int deaths = 0;
    private AbstractGame session;
    private FileConfiguration conf;
    private Team team;
    private Kit kit;
    private int tickets;
    private List<String> kits = Lists.newArrayList();
    private List<Unlockable> activeUnlockables = Lists.newArrayList();
    private List<Unlockable> unlockables = Lists.newArrayList();
    private String uuid;

    public MGPlayer(Player p, FileConfiguration f) {
        this.session = null;
        this.team = null;
        this.kit = null;
        this.conf = f;
        if(conf.isSet(ConfigValues.AVAILABLE_KITS.getPath()))this.kits = conf.getStringList(ConfigValues.AVAILABLE_KITS.getPath());
        this.tickets = conf.getInt(ConfigValues.TICKETS.getPath(), 0);
        if(conf.isSet(ConfigValues.UNLOCKABLES.getPath()))
        {
            for(String s : conf.getStringList(ConfigValues.UNLOCKABLES.getPath()))
            {
                try {
                    unlockables.add(Unlockable.valueOf(s));
                } catch (Exception ignored){}
            }
        }
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

    public int getTickets()
    {
        return tickets;
    }

    public void setTickets(int tickets)
    {
        this.tickets = tickets;
    }

    public void addTickets(int added)
    {
        ChatUtil.sendGameMessage(getPlayer(), ChatColor.LIGHT_PURPLE + "You earned " + ChatColor.DARK_PURPLE + added + ChatColor.LIGHT_PURPLE + " tickets!");
        this.tickets = tickets + added;
    }

    public boolean removeTickets(int removed)
    {
        if(tickets - removed >= 0)
        {
            tickets = tickets - removed;
            return true;
        }
        return false;
    }

    public void addUnlockable(Unlockable u)
    {
        unlockables.add(u);
    }

    public boolean hasUnlockable(Unlockable u)
    {
        return unlockables.contains(u);
    }

    public List<String> getKits()
    {
        return kits;
    }

    public void addKit(String kitName)
    {
        kits.add(kitName);
    }

    public boolean hasKit(String kit)
    {
        return kits.contains(kit);
    }

    public void removeKit(String kitname)
    {
        if(kits.contains(kitname))kits.remove(kitname);
    }

    public List<Unlockable> getUnlockables()
    {
        return unlockables;
    }

    public List<Unlockable> getActiveUnlockables()
    {
        return activeUnlockables;
    }

    public void removeUnlockable(Unlockable u)
    {
        if(hasUnlockable(u))unlockables.remove(u);
    }

    public void setSession(AbstractGame game)
    {
        this.session = game;
    }

    public void giveWatch()
    {
        getPlayer().getInventory().setItem(0, watch);
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

    public void clearInventory()
    {
        getPlayer().getInventory().setContents(new ItemStack[]{});
        getPlayer().getInventory().setArmorContents(new ItemStack[]{null, null, null, null});
        getPlayer().updateInventory();
    }

    public void clearActiveUnlockables()
    {
        ChatUtil.sendGameMessage(getPlayer(), "You disabled all active unlockables!");
        activeUnlockables.clear();
    }

    public void removeActiveUnlockable(Unlockable remove)
    {
        ChatUtil.sendGameMessage(getPlayer(), remove.getName() + " is now no longer activated!");
        if(activeUnlockables.contains(remove))activeUnlockables.remove(remove);
    }

    public void addActiveUnlockable(Unlockable added)
    {
        ChatUtil.sendGameMessage(getPlayer(), added.getName() + " is now active!");
        activeUnlockables.add(added);
    }

    public void purgeStats()
    {
        this.session = null;
        if(team != null && team.getPlayers().contains(this)) team.getPlayers().remove(this);
        this.team = null;
        this.deaths = 0;
        this.kit = null;
        this.kills = 0;
        ScoreboardUtil.setHubBoard(getPlayer());

        clearPotions();
    }

    public void clearPotions()
    {
        getPlayer().getActivePotionEffects().stream().forEach(e ->
        {
            getPlayer().removePotionEffect(e.getType());
        });
    }

    private void updateConfig()
    {
        List<String> unlockablesNames = Lists.newArrayList();
        unlockables.stream().forEach(u -> unlockablesNames.add(u.name()));
        conf.set(ConfigValues.UNLOCKABLES.getPath(), unlockablesNames);
        conf.set(ConfigValues.TICKETS.getPath(), tickets);
        conf.set(ConfigValues.AVAILABLE_KITS.getPath(), kits);
    }

    public void saveConfig()
    {
        updateConfig();
        ConfigManager.savePlayerFile(getPlayer(), conf);

    }
}
