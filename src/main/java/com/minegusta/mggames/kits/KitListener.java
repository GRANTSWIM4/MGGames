package com.minegusta.mggames.kits;

import com.minegusta.mggames.player.MGPlayer;
import com.minegusta.mggames.register.Register;
import com.minegusta.mggames.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class KitListener implements Listener {

    @EventHandler
    public void onPlayerKitSelect(PlayerInteractEvent e)
    {

        if(!e.hasBlock() || e.getClickedBlock().getType() != Material.WALL_SIGN) return;

        Sign sign = (Sign) e.getClickedBlock().getState();

        String line1 = sign.getLine(0);

        if(!line1.equalsIgnoreCase("[Kit]"))return;

        if(sign.getLine(1).length() == 0) return;

        String string = sign.getLine(1);

        MGPlayer mgp = Register.getPlayer(e.getPlayer());

        Kit kit;

        if(!KitRegistry.exists(string))
        {
            return;
        }
        kit = KitRegistry.getKit(string);


        if(mgp.getSession() == null || !mgp.getSession().getAvailableKits().contains(string)) return;

        if(kit.isDefault() || mgp.hasKit(kit.getName()))
        {
            mgp.setKit(kit);
            ChatUtil.sendGameMessage(mgp.getPlayer(), "You selected " + kit.getName() + " as your class.");
        }
        else
        {
            ChatUtil.sendFormattedMessage(mgp.getPlayer(), "You have not unlocked that kit!", "Unlock it in the kit shop in the hub.");
        }
    }
}
