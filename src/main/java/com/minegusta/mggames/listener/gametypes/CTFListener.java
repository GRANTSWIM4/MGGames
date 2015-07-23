package com.minegusta.mggames.listener.gametypes;

import com.minegusta.mggames.game.GameTypes;
import com.minegusta.mggames.game.Stage;
import com.minegusta.mggames.game.TeamType;
import com.minegusta.mggames.gametype.CaptureTheFlag;
import com.minegusta.mggames.player.MGPlayer;
import com.minegusta.mggames.register.Register;
import com.minegusta.mggames.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class CTFListener implements Listener
{

    //Stop drops on death in ctf
    @EventHandler
    public void onDeath(PlayerDeathEvent e)
    {
        MGPlayer mgp = Register.getPlayer(e.getEntity());
        if(mgp.getSession() != null && mgp.getSession().getGameType() == GameTypes.CTF)
        {
            e.getDrops().clear();
            e.getEntity().getInventory().clear();
            e.setDroppedExp(0);

            CaptureTheFlag ctf = (CaptureTheFlag) mgp.getSession();

            ctf.onDeath(mgp);
        }
    }

    //Stop dropping of items in ctf
    @EventHandler
    public void onDrop(PlayerDropItemEvent e)
    {
        Player p = e.getPlayer();
        MGPlayer mgp = Register.getPlayer(p);

        if(mgp.getSession() == null || mgp.getSession().getGameType() != GameTypes.CTF) return;

        ChatUtil.sendFormattedMessage(p, "You cannot drop items in CTF!");
        e.setCancelled(true);
    }

    //Listen for block hit and flag score
    @EventHandler
    public void onFlagHit(PlayerInteractEvent e)
    {
        if(!e.hasBlock())return;

        Player p = e.getPlayer();
        Block clicked = e.getClickedBlock();
        MGPlayer mgp = Register.getPlayer(p);

        if(mgp.getSession() == null || mgp.getSession().getGameType() != GameTypes.CTF || mgp.getSession().getStage() != Stage.PLAYING)return;

        TeamType team = mgp.getTeam().getType();

        CaptureTheFlag ctf = (CaptureTheFlag) mgp.getSession();

        //Taking the blue wool
        if(team == TeamType.RED && !ctf.isBlueWoolTaken() && locationsMatch(ctf.getBlueWool(), clicked.getLocation()))
        {
            ctf.takeBlueWool(mgp);
        }
        //Taking the red wool
        if(team == TeamType.BLUE && !ctf.isRedWoolTaken() && locationsMatch(clicked.getLocation(), ctf.getRedWool()))
        {
            ctf.takeRedWool(mgp);
        }

        //Scoring a red point
        if(team == TeamType.RED && !ctf.isRedWoolTaken() && ctf.isBlueWoolTaken() && locationsMatch(clicked.getLocation(), ctf.getRedWool()) && ctf.getBlueFlagCarrier() == mgp)
        {
            ctf.returnBlueWool();
            ctf.addScore(TeamType.RED, mgp);
        }

        //Scoring a blue point
        if(team == TeamType.BLUE && !ctf.isBlueWoolTaken() && ctf.isRedWoolTaken() && locationsMatch(clicked.getLocation(), ctf.getBlueWool()) && ctf.getRedFlagCarrier() == mgp)
        {
            ctf.returnRedWool();
            ctf.addScore(TeamType.BLUE, mgp);
        }
    }

    private boolean locationsMatch(Location l1, Location l2)
    {
        return ((int) l1.getX() == (int) l2.getX()) && ((int) l1.getY() == (int) l2.getY()) && ((int) l1.getZ() == (int) l2.getZ()) && (l1.getWorld().getName().equals(l2.getWorld().getName()));
    }
}
