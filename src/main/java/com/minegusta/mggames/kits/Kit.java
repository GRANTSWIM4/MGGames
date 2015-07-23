package com.minegusta.mggames.kits;

import com.minegusta.mggames.register.Register;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class Kit
{
    private List<MGItem> items;
    private List<PotionEffect> effects;
    private int cost;
    private boolean isDefault;
    private String name;

    public Kit(String name, List<MGItem> items, List<PotionEffect> effects, boolean isDefault, int cost)
    {
        this.cost = cost;
        this.isDefault = isDefault;
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
            int slot = i.getSlot();
            if(slot > 99)
            {
                switch (slot)
                {
                    case 100: p.getInventory().setBoots(i.buildItem());
                        break;
                    case 101: p.getInventory().setLeggings(i.buildItem());
                        break;
                    case 102: p.getInventory().setChestplate(i. buildItem());
                        break;
                    case 103: p.getInventory().setHelmet(i.buildItem());
                        break;
                }
            }
            else {
                p.getInventory().setItem(i.getSlot(), i.buildItem());
            }
        }

        p.updateInventory();

        Register.getPlayer(p).clearPotions();

        applyEffects(p);
    }

    public boolean isDefault()
    {
        return isDefault;
    }

    public int getCost()
    {
        return cost;
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
