package com.minegusta.mggames.rewards.instances;

import com.minegusta.mggames.rewards.UnlockableItem;
import com.minegusta.mggames.rewards.UnlockableType;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class FireTrail implements UnlockableItem {

    @Override
    public void apply(Player p) {
        p.getWorld().spigot().playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 0, 0, 1, 0.4F, 1, 1/5, 15, 25);
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
        return ChatColor.RED + "Fire Trail";
    }

    @Override
    public String[] getLore() {
        return new String[]{"A trail of fire", "follows you around.", "Can be toggled."};
    }

    @Override
    public int getCost() {
        return 350;
    }

    @Override
    public Material getMaterial() {
        return Material.FIREBALL;
    }

    @Override
    public int getData() {
        return 0;
    }
}
