package com.minegusta.mggames.rewards;

import com.minegusta.mggames.rewards.instances.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public enum Unlockable
{
    FIRE_TRAIL(new FireTrail()),
    SMOKE_TRAIL(new SmokeTrail()),
    RAINBOW_TRAIL(new RainbowTrail()),
    Note_TRAIL(new NoteTrail()),
    ENDER_TRAIL(new EnderTrail()),
    PURPLE_TRAIN(new PurpleTrail());

    private UnlockableItem i;

    private Unlockable(UnlockableItem i)
    {
        this.i = i;
    }

    public ItemStack buildShopItem()
    {
        return i.buildShopItem();
    }

    public ItemStack buildUnlockedItem(boolean enabled)
    {
        return i.buildUnlockedItem(enabled);
    }

    public String getName()
    {
        return i.getName();
    }

    public UnlockableType getType()
    {
        return i.getType();
    }

    public int getData()
    {
        return i.getData();
    }

    public String[] getLore()
    {
        return i.getLore();
    }

    public int getCost()
    {
        return i.getCost();
    }

    public void apply(Player p)
    {
        i.apply(p);
    }

    public void apply(Event event)
    {
        i.apply(event);
    }


}
