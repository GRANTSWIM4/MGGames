package com.minegusta.mggames.rewards.instances;

import com.minegusta.mggames.rewards.UnlockableItem;
import com.minegusta.mggames.rewards.UnlockableType;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class EnderTrail implements UnlockableItem {
    @Override
    public void apply(Player p) {
        p.getWorld().spigot().playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 0, 0, 0.5F, 0.4F, 0.5F, 1/5, 15, 25);

    }

    @Override
    public void apply(Event event) {

    }

    @Override
    public UnlockableType getType() {
        return UnlockableType.TIMED;
    }

    @Override
    public String getName() {
        return ChatColor.DARK_PURPLE + "Ender Trail";
    }

    @Override
    public String[] getLore() {
        return new String[]{"Ender particles", "follow you around.", "Can be toggled."};
    }

    @Override
    public int getCost() {
        return 325;
    }

    @Override
    public Material getMaterial() {
        return Material.ENDER_PEARL;
    }

    @Override
    public int getData() {
        return 0;
    }
}
