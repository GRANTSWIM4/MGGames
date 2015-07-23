package com.minegusta.mggames.rewards.instances;

import com.minegusta.mggames.rewards.UnlockableItem;
import com.minegusta.mggames.rewards.UnlockableType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class NoteTrail implements UnlockableItem {
    @Override
    public void apply(Player p) {
        p.getWorld().spigot().playEffect(p.getLocation(), Effect.NOTE, 0, 0, 1, 0.4F, 0, 1/5, 15, 25);

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
        return ChatColor.BLUE + "Note Trail";
    }

    @Override
    public String[] getLore() {
        return new String[]{"Music notes", "follow you around.", "Can be toggled."};
    }

    @Override
    public int getCost() {
        return 315;
    }

    @Override
    public Material getMaterial() {
        return Material.JUKEBOX;
    }

    @Override
    public int getData() {
        return 0;
    }
}
