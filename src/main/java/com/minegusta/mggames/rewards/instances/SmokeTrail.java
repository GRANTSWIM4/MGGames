package com.minegusta.mggames.rewards.instances;

import com.minegusta.mggames.rewards.UnlockableItem;
import com.minegusta.mggames.rewards.UnlockableType;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class SmokeTrail implements UnlockableItem {
    @Override
    public void apply(Player p) {
        p.getWorld().spigot().playEffect(p.getLocation(), Effect.LARGE_SMOKE, 0, 0, 0.5F, 0.4F, 0.5F, 1/5, 15, 25);

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
        return ChatColor.DARK_GRAY + "Smoke Trail";
    }

    @Override
    public String[] getLore() {
        return new String[]{"A smoke trail", "follows you around.", "Can be toggled."};
    }

    @Override
    public int getCost() {
        return 300;
    }

    @Override
    public Material getMaterial() {
        return Material.SULPHUR;
    }

    @Override
    public int getData() {
        return 0;
    }
}
