package com.minegusta.mggames.sign;

import com.minegusta.mggames.game.AbstractGame;
import com.minegusta.mggames.game.Stage;
import com.minegusta.mggames.main.Main;
import com.minegusta.mggames.player.MGPlayer;
import com.minegusta.mggames.register.Register;
import com.minegusta.mggames.util.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

public class SignListener implements Listener {

    @EventHandler
    public void onSignClick(PlayerInteractEvent e)
    {
        if(e.hasBlock() && e.getPlayer().getWorld() == Main.getHub() && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.WALL_SIGN)
        {
            Sign sign = (Sign) e.getClickedBlock().getState();

            if(ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[Join]"))
            {
                String instanceName = ChatColor.stripColor(sign.getLine(1));
                if(instanceName.length() == 0) return;

                Optional<String> found = Register.getSessionNames().stream().filter(name -> name.equalsIgnoreCase(instanceName)).findFirst();
                if(!found.isPresent())return;

                AbstractGame game = Register.getGame(found.get());

                Player p = e.getPlayer();
                MGPlayer mgp = Register.getPlayer(p);

                if(game.getPlayers().size() >= game.getMaxPlayers())
                {
                    ChatUtil.sendFormattedMessage(p, "That game is full!");
                    return;
                }

                if(game.getStage() == Stage.LOBBY)
                {
                    if(mgp.getSession() != null)
                    {
                        mgp.getSession().onPlayerLeave(mgp);
                    }
                    game.onPlayerJoin(mgp);
                } else {
                    ChatUtil.sendFormattedMessage(p, "That game already started!", "Please join another game or wait a while.");
                }
            }
            else if(ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[Leave]"))
            {
                MGPlayer mgp = Register.getPlayer(e.getPlayer());
                if(mgp.getSession() != null)
                {
                    mgp.getSession().onPlayerLeave(mgp);
                }
            }
        }
    }
}
