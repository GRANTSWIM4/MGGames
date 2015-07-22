package com.minegusta.mggames.player;

import com.google.common.collect.Lists;
import com.minegusta.mggames.game.AbstractGame;
import com.minegusta.mggames.game.Team;
import com.minegusta.mggames.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

    public void purgeStats()
    {
        this.session = null;
        if(team != null && team.getPlayers().contains(this)) team.getPlayers().remove(this);
        this.team = null;
        this.deaths = 0;
        this.kit = null;
        this.kills = 0;

        clearPotions();
    }

    public void clearPotions()
    {
        getPlayer().getActivePotionEffects().stream().forEach(e ->
        {
            getPlayer().removePotionEffect(e.getType());
        });
    }
}
