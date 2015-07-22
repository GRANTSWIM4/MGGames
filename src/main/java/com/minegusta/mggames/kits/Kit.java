package com.minegusta.mggames.kits;

import com.minegusta.mggames.register.Register;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;
import java.util.List;

public class Kit
{
    private List<MGItem> items;
    private List<PotionEffect> effects;
    private String name;

    public Kit(String name, List<MGItem> items, List<PotionEffect> effects)
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
        p.getInventory().setContents(new ItemStack[]{});
        p.getInventory().setArmorContents(new ItemStack[]{null, null, null, null});

        for(MGItem i : items)
        {
            p.getInventory().setItem(i.getSlot(), i.buildItem());
        }

        p.updateInventory();

        Register.getPlayer(p).clearPotions();

        applyEffects(p);
    }

    public void applyEffects(Player p)
    {
        if(effects.size() == 0) return;
        effects.stream().forEach(e ->
        {
            p.removePotionEffect(e.getType());
            p.addPotionEffect(e);
        });
    }

    public List<PotionEffect> getEffects()
    {
        return effects;
    }
}
