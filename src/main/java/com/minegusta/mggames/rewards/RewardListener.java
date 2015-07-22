package com.minegusta.mggames.rewards;

import com.minegusta.mggames.player.MGPlayer;
import com.minegusta.mggames.register.Register;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class RewardListener implements Listener
{
    //Reward menu listener for activating and disabling
    @EventHandler
    public void onRewardClick(InventoryClickEvent e)
    {
        if(!(e.getWhoClicked() instanceof Player))return;

        if(e.getClickedInventory() == null || !RewardMenu.invs.contains(e.getClickedInventory())) return;

        if(e.getCurrentItem() == null)return;

        e.setCancelled(true);

        Player p = (Player) e.getWhoClicked();
        MGPlayer mgp = Register.getPlayer(p);
        ItemStack clicked = e.getCurrentItem();
        Unlockable item = null;

        if(!clicked.hasItemMeta() || !clicked.getItemMeta().hasDisplayName())return;

        String name = clicked.getItemMeta().getDisplayName();

        //Check for clear all
        if(name.equals(RewardMenu.clear.getItemMeta().getDisplayName()))
        {

            mgp.clearActiveUnlockables();
            p.closeInventory();
            RewardMenu.openRewardMenu(p);
            return;
        }

        //Check for unlockables
        for(Unlockable u : Unlockable.values())
        {
            if(u.getName().equals(name))
            {
                item = u;
                break;
            }
        }
        if(item == null) return;

        if(mgp.getActiveUnlockables().contains(item))
        {
            mgp.removeActiveUnlockable(item);
        }
        else
        {
            mgp.addActiveUnlockable(item);
        }
        p.closeInventory();
        RewardMenu.openRewardMenu(p);
    }

    //Remove the inventory from the collection
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e)
    {
        if(RewardMenu.invs.contains(e.getInventory()))
        {
            RewardMenu.invs.remove(e.getInventory());
        }
    }

    //Listen for the reward powers on events

}
