package com.minegusta.mggames.listener;

import com.google.common.collect.Lists;
import com.minegusta.mggames.game.Stage;
import com.minegusta.mggames.main.Main;
import com.minegusta.mggames.player.MGPlayer;
import com.minegusta.mggames.register.Register;
import com.minegusta.mggames.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

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
    @EventHandler
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

            String cause = "";
            if(e.getDamager() instanceof Player)
            {
                cause = e.getDamager().getName();
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
            mgp.getSession().onRespawn(mgp, "");
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByBlockEvent e)
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

            if(mgp.getSession().getStage() == Stage.PLAYING && p.getHealth() - e.getDamage() <= 0)
            {
                e.setCancelled(true);
                mgp.getSession().onRespawn(mgp, p.getName() + " died because of block damage!");
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e)
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
            if(mgp.getSession().getStage() == Stage.PLAYING && p.getHealth() - e.getDamage() <= 0)
            {
                e.setCancelled(true);
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
}
