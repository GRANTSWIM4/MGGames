package com.minegusta.mggames.main;

import com.minegusta.mggames.kits.KitListener;
import com.minegusta.mggames.listener.GlobalListener;
import com.minegusta.mggames.listener.gametypes.CTFListener;
import com.minegusta.mggames.rewards.RewardListener;
import com.minegusta.mggames.rewards.ShopListener;
import com.minegusta.mggames.sign.SignListener;

public enum Listener
{
    LISTENER1(new GlobalListener()),
    CTF(new CTFListener()),
    KIT(new KitListener()),
    REWARD_LISTENER(new RewardListener()),
    SHOP_LISTENER(new ShopListener()),
    SIGN(new SignListener());

    private org.bukkit.event.Listener l;

    Listener(org.bukkit.event.Listener l)
    {
        this.l = l;
    }

    public org.bukkit.event.Listener getListener()
    {
        return l;
    }
}
