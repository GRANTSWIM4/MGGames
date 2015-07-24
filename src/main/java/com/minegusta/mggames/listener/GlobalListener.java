package com.minegusta.mggames.listener;

import com.google.common.collect.Lists;
import com.minegusta.mggames.game.GameTypes;
import com.minegusta.mggames.game.Stage;
import com.minegusta.mggames.game.TeamType;
import com.minegusta.mggames.main.Main;
import com.minegusta.mggames.player.MGPlayer;
import com.minegusta.mggames.register.Register;
import com.minegusta.mggames.util.ChatUtil;
import com.minegusta.mggames.util.ScoreboardUtil;
import javafx.scene.layout.Priority;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;

public class GlobalListener implements Listener {

    //On join add people to default and send them to hub
    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        Register.registerPlayer(e.getPlayer());

        if(e.getPlayer().getWorld() != Main.getHub())
        {
            e.getPlayer().teleport(Main.getHub().getSpawnLocation());
        }
        ChatUtil.sendFormattedMessage(e.getPlayer(), ChatColor.YELLOW + "Use " + ChatColor.LIGHT_PURPLE + "/Tickets " + ChatColor.YELLOW + "to view your tickets.", ChatColor.YELLOW + "Use " + ChatColor.LIGHT_PURPLE + "/Rewards " + ChatColor.YELLOW + "to toggle your active perks.");
        ScoreboardUtil.setHubBoard(e.getPlayer());
        e.getPlayer().setFoodLevel(20);
        e.getPlayer().setHealth(e.getPlayer().getMaxHealth());
        e.setJoinMessage(null);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e)
    {
        e.setDeathMessage(null);
    }

    //On leave remove people from games and send them to hub
    @EventHandler
    public void onLeave(PlayerQuitEvent e)
    {
        e.setQuitMessage(null);
        MGPlayer mgp = Register.getPlayer(e.getPlayer().getUniqueId().toString());

        if(mgp.getSession() != null) mgp.getSession().onPlayerLeave(mgp);

        if(e.getPlayer().getWorld() != Main.getHub())
        {
            e.getPlayer().teleport(Main.getHub().getSpawnLocation());
        }

        mgp.saveConfig();

        Register.removePlayer(e.getPlayer().getUniqueId().toString());
    }

    //Stop people from dropping items in the lobby and in other places
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e)
    {
        MGPlayer mgp = Register.getPlayer(e.getPlayer());

        if(mgp.getSession() == null || mgp.getSession().getStage() == Stage.LOBBY)
        {
            e.setCancelled(true);
        }
    }

    //Stop damage in the lobby and hub
    @EventHandler(priority = EventPriority.LOW)
    public void onDamage(EntityDamageByEntityEvent e)
    {
        if(e.getEntity() instanceof Player)
        {
            Player p = (Player) e.getEntity();
            MGPlayer mgp = Register.getPlayer(p);

            if(mgp.getSession() == null || mgp.getSession().getStage() == Stage.LOBBY || mgp.getSession().getStage() == Stage.GRACE)
            {
                e.setCancelled(true);
                return;
            }

            if(e.isCancelled())return;

            String cause = "";
            if(e.getDamager() instanceof Player)
            {
                cause = e.getDamager().getName();
            }
            if(e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player)
            {
                cause = ((Player) ((Arrow) e.getDamager()).getShooter()).getName();
            }

            if(mgp.getSession().getStage() == Stage.PLAYING && p.getHealth() - e.getDamage() <= 0)
            {
                e.setCancelled(true);
                mgp.getSession().onRespawn(mgp, p.getName() + " was killed by " + cause + ".");
            }
        }
    }

    //Respawn player in the correct spot
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e)
    {
        Player p = e.getPlayer();
        MGPlayer mgp = Register.getPlayer(p);

        if(mgp.getSession() == null)
        {
            p.teleport(Main.getHub().getSpawnLocation());
        }
        else
        {
            e.setRespawnLocation(mgp.getTeam().getSpawn());
            mgp.getSession().onRespawn(mgp, null);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamage(EntityDamageByBlockEvent e)
    {
        if(e.isCancelled())return;
        if(e.getEntity() instanceof Player)
        {
            Player p = (Player) e.getEntity();
            MGPlayer mgp = Register.getPlayer(p);

            if(mgp.getSession() == null || mgp.getSession().getStage() == Stage.LOBBY || mgp.getSession().getStage() == Stage.GRACE)
            {
                e.setCancelled(true);
                return;
            }

            if(mgp.getSession().getStage() == Stage.PLAYING && p.getHealth() - e.getDamage() <= 0)
            {
                e.setCancelled(true);
                mgp.getSession().onRespawn(mgp, p.getName() + " died because of block damage!");
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamage(EntityDamageEvent e)
    {
        if(e.isCancelled())return;

        if(e.getEntity() instanceof Player)
        {
            Player p = (Player) e.getEntity();
            MGPlayer mgp = Register.getPlayer(p);

            if(mgp.getSession() == null || mgp.getSession().getStage() == Stage.LOBBY || mgp.getSession().getStage() == Stage.GRACE)
            {
                e.setCancelled(true);
                return;
            }

            if(mgp.getSession().getStage() == Stage.PLAYING && p.getHealth() - e.getDamage() <= 0)
            {
                e.setCancelled(true);
                p.setFireTicks(0);
                for(PotionEffect f : p.getActivePotionEffects())
                {
                    p.removePotionEffect(f.getType());
                }
                mgp.getSession().onRespawn(mgp, p.getName() + " died!");
            }

        }
    }

    //Block arrows in the lobby
    @EventHandler
    public void onArrowShoot(EntityShootBowEvent e)
    {
        if(e.getEntity() instanceof Player)
        {
            MGPlayer mgp = Register.getPlayer((Player) e.getEntity());
            if(mgp.getSession() != null && mgp.getSession().getStage() == Stage.LOBBY)
            {
                ChatUtil.sendFormattedMessage(mgp.getPlayer(), "You cannot use that in the lobby!");
                e.setCancelled(true);
            }
        }
    }

    private static final List<Material> blockInLobby = Lists.newArrayList(Material.POTION, Material.SNOW_BALL, Material.GOLDEN_APPLE, Material.APPLE, Material.PORK, Material.CARROT, Material.COOKED_BEEF, Material.COOKED_CHICKEN, Material.COOKED_FISH, Material.GRILLED_PORK, Material.COOKIE);

    //BLock potions in the lobby
    @EventHandler
    public void onPotionThrow(PlayerInteractEvent e)
    {
        MGPlayer mgp = Register.getPlayer(e.getPlayer());

        if((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && mgp.getSession() != null && mgp.getSession().getStage() == Stage.LOBBY)
        {
            if(blockInLobby.contains(e.getPlayer().getItemInHand().getType()))
            {
                ChatUtil.sendFormattedMessage(e.getPlayer(), "You cannot use that item in the lobby!");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e)
    {
        MGPlayer mgp = Register.getPlayer(e.getPlayer());

        if(mgp.getSession() != null && mgp.getSession().getStage() == Stage.LOBBY)
        {
            if(blockInLobby.contains(e.getPlayer().getItemInHand().getType()))
            {
                ChatUtil.sendFormattedMessage(e.getPlayer(), "You cannot use that item in the lobby!");
                e.setCancelled(true);
            }
        }
    }

    //block team damage
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTeamDamage(EntityDamageByEntityEvent e)
    {
        if(e.isCancelled())return;
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player)
        {
            Player damager = (Player) e.getDamager();
            Player victim = (Player) e.getEntity();
            MGPlayer damagerMGP = Register.getPlayer(damager);
            MGPlayer victimMGP = Register.getPlayer(victim);

            if(damagerMGP.getTeam() == null ||damagerMGP.getTeam().getType() == TeamType.FFA || victimMGP.getTeam() == null || victimMGP.getTeam().getType() == TeamType.FFA) return;

            if(damagerMGP.getTeam().getType() == victimMGP.getTeam().getType())
            {
                e.setCancelled(true);
                return;
            }
        }
        else if(e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player && e.getEntity() instanceof Player)
        {
            Player damager = (Player) ((Arrow) e.getDamager()).getShooter();
            Player victim = (Player) e.getEntity();
            MGPlayer damagerMGP = Register.getPlayer(damager);
            MGPlayer victimMGP = Register.getPlayer(victim);

            if(damagerMGP.getTeam() == null || damagerMGP.getTeam().getType() == TeamType.FFA || victimMGP.getTeam() == null || victimMGP.getTeam().getType() == TeamType.FFA) return;

            if(damagerMGP.getTeam().getType() == victimMGP.getTeam().getType())
            {
                e.setCancelled(true);
                return;
            }
        }
    }

    //Stop placing and breaking blocks if disallowed
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e)
    {
        Player p = e.getPlayer();
        MGPlayer mgp = Register.getPlayer(p);

        if(mgp.getSession() != null && !mgp.getSession().allowBreaking())
        {
            e.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e)
    {
        Player p = e.getPlayer();
        MGPlayer mgp = Register.getPlayer(p);

        if(mgp.getSession() != null && !mgp.getSession().allowPlacing())
        {
            e.setCancelled(true);
            return;
        }
    }

    //Block mobs from spawning naturally
    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent e)
    {
        if(e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM && e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER_EGG)
        {
            e.setCancelled(true);
        }
    }
}
