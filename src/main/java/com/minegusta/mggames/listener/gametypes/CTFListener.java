package com.minegusta.mggames.listener.gametypes;

import com.minegusta.mggames.game.GameTypes;
import com.minegusta.mggames.game.Stage;
import com.minegusta.mggames.game.TeamType;
import com.minegusta.mggames.gametype.CaptureTheFlag;
import com.minegusta.mggames.player.MGPlayer;
import com.minegusta.mggames.register.Register;
import com.minegusta.mggames.util.ChatUtil;
import org.bukkit.Bukkit;
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
        Bukkit.broadcastMessage("Running flag event...");
        if(!e.hasBlock())return;

        Player p = e.getPlayer();
        Block clicked = e.getClickedBlock();
        MGPlayer mgp = Register.getPlayer(p);

        if(mgp.getSession() == null || mgp.getSession().getGameType() != GameTypes.CTF || mgp.getSession().getStage() != Stage.PLAYING)return;

        Bukkit.broadcastMessage("CTF interact with block detected!");

        TeamType team = mgp.getTeam().getType();

        CaptureTheFlag ctf = (CaptureTheFlag) mgp.getSession();

        Bukkit.broadcastMessage("blue taken: " + ctf.isBlueWoolTaken());
        Bukkit.broadcastMessage("team interactor: " + team.name());
        Bukkit.broadcastMessage("Wool block = clicked: " + Boolean.toString(ctf.getBlueWool().getBlock() == clicked));

        //Taking the blue wool
        if(team == TeamType.RED && !ctf.isBlueWoolTaken() && ctf.getBlueWool().getBlock() == clicked)
        {
            ctf.setBlueFlagCarrier(mgp);
            ctf.takeBlueWool(mgp);
        }
        //Taking the red wool
        if(team == TeamType.BLUE && !ctf.isRedWoolTaken() && clicked == ctf.getRedWool().getBlock())
        {
            ctf.setRedFlagCarrier(mgp);
            ctf.takeRedWool(mgp);
        }

        //Scoring a red point
        if(team == TeamType.RED && !ctf.isRedWoolTaken() && ctf.isBlueWoolTaken() && clicked == ctf.getRedWool().getBlock() && ctf.getBlueFlagCarrier() == mgp)
        {
            ctf.returnBlueWool();
            ctf.addScore(TeamType.RED, mgp);
        }

        //Scoring a blue point
        if(team == TeamType.BLUE && !ctf.isBlueWoolTaken() && ctf.isRedWoolTaken() && clicked == ctf.getBlueWool().getBlock() && ctf.getRedFlagCarrier() == mgp)
        {
            ctf.returnRedWool();
            ctf.addScore(TeamType.BLUE, mgp);
        }
    }
}
