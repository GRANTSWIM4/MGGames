package com.minegusta.mggames.rewards;

import com.minegusta.mggames.player.MGPlayer;
import com.minegusta.mggames.register.Register;
import com.minegusta.mggames.tasks.ShopTask;
import com.minegusta.mggames.util.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ShopListener implements Listener{

    //Block clicks in the inventory and apply the buying
    @EventHandler
    public void onBuy(InventoryClickEvent e)
    {
        if(!(e.getWhoClicked() instanceof Player))return;

        Player p = (Player) e.getWhoClicked();

        if(e.getClickedInventory() == null || !ShopTask.contains(e.getClickedInventory()))return;

        e.setCancelled(true);

        if(e.getCurrentItem() == null) return;

        ItemStack clicked = e.getCurrentItem();
        Inventory inv = e.getInventory();
        Unlockable reward = null;
        MGPlayer mgp = Register.getPlayer(p);

        for(Unlockable u : Unlockable.values())
        {
            ItemStack stack = u.buildShopItem();
            if(stack.getType() == clicked.getType() && stack.getData() == clicked.getData())
            {
                if(clicked.hasItemMeta() && clicked.getItemMeta().hasDisplayName() && clicked.getItemMeta().getDisplayName().equals(stack.getItemMeta().getDisplayName()))
                {
                    reward = u;
                    break;
                }
            }
        }
        if(reward == null)
        {
            return;
        }
        if(mgp.hasUnlockable(reward))
        {
            ChatUtil.sendFormattedMessage(p, "You already bought that item!");
            return;
        }
        if(mgp.removeTickets(reward.getCost()))
        {
            ChatUtil.sendFormattedMessage(p, "You bought "+ reward.getName() + "!");
            p.closeInventory();
            return;
        }
        else
        {
            ChatUtil.sendFormattedMessage(p, "You cannot afford that item!");
            return;
        }


    }

    //Trading villager
    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent e)
    {
        if(e.getRightClicked() instanceof Villager)
        {
            e.setCancelled(true);
            ShopMenu.openShop(e.getPlayer());
        }
    }

    //Villagers can only be killed by Opped players.
    @EventHandler
    public void onVillagerdamage(EntityDamageByEntityEvent e)
    {
        if(e.getEntity() instanceof Villager)
        {
            if(e.getDamager() instanceof Player && e.getDamager().isOp())
            {
                e.setCancelled(false);
                return;
            }
            e.setCancelled(true);
        }
    }
}
