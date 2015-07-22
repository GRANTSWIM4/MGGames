package com.minegusta.mggames.kits;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.minegusta.mggames.config.ConfigManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class KitRegistry {
    public static ConcurrentMap<String, Kit> kits = Maps.newConcurrentMap();

    public static void register(String name, Kit kit)
    {
        kits.put(name, kit);
    }

    public static Kit getKit(String name)
    {
        return kits.get(name);
    }

    public static boolean exists(String name)
    {
        return kits.containsKey(name);
    }

    public static void removeKit(String name)
    {
        if(exists(name))kits.remove(name);
    }

    public static void loadKits()
    {
        FileConfiguration f = ConfigManager.getKitFile();

        f.getKeys(false).stream().forEach(kit ->
        {

            List<MGItem> items = Lists.newArrayList();
            List<PotionEffect> effects = Lists.newArrayList();

            f.getConfigurationSection(kit).getKeys(false).forEach(item ->
            {
                if (item.equalsIgnoreCase("potion-effects"))
                {
                    f.getConfigurationSection(kit + "." + item).getKeys(false).stream().forEach(effect ->
                    {
                        int amplifier = f.getInt(kit + "." + item + "." + effect, 0);
                        PotionEffectType type;

                        try {
                            type = PotionEffectType.getByName(effect.toUpperCase());
                        } catch (Exception ignored)
                        {
                            type = PotionEffectType.SATURATION;
                        }

                        PotionEffect potionEffect = new PotionEffect(type, 20 * 25, amplifier);
                        effects.add(potionEffect);
                    });
                } else {
                    String name = item;
                    String lore = f.getString(kit + "." + item + ".lore");
                    int slot = f.getInt(kit + "." + item + ".slot");
                    int amount = f.getInt(kit + "." + item + ".amount");
                    int data = f.getInt(kit + "." + item + ".data");
                    Material material;
                    try{
                        material = Material.valueOf(f.getString(kit + "." + item + ".material"));
                    } catch (Exception ignored)
                    {
                        material = Material.POTATO;
                    }

                    MGItem stack = new MGItem(name, slot, material, amount, data, lore);
                    items.add(stack);
                }
            });

            register(kit, new Kit(kit, (MGItem[]) items.toArray(), (PotionEffect[]) effects.toArray()));

        });
    }
}
