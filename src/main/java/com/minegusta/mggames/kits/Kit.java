package com.minegusta.mggames.kits;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;

public class Kit
{
    private MGItem[] items;
    private PotionEffect[] effects;
    private String name;

    public Kit(String name, MGItem[] items, PotionEffect[] effects)
    {
        this.name = name;
        this.items = items;
        this.effects = effects;
    }

    public String getName()
    {
        return name;
    }

    public void apply(Player p)
    {
         p.getInventory().clear();

        for(MGItem i : items)
        {
            p.getInventory().setItem(i.getSlot(), i.buildItem());
        }

        applyEffects(p);
    }

    public void applyEffects(Player p)
    {
        if(effects.length == 0) return;
        Arrays.asList(effects).stream().forEach(e ->
        {
            p.removePotionEffect(e.getType());
            p.addPotionEffect(e);
        });
    }

    public PotionEffect[] getEffects()
    {
        return effects;
    }
}
