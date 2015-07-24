package com.minegusta.mggames.kits;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.minegusta.mggames.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class KitRegistry {
    public static ConcurrentMap<String, Kit> kits = Maps.newConcurrentMap();

    public static void register(String name, Kit kit)
    {
        kits.put(name, kit);
    }

    public static void clearKits()
    {
        kits.clear();
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

    public static Collection<Kit> getKits()
    {
        return kits.values();
    }

    public static void loadKits()
    {
        FileConfiguration f = ConfigManager.getKitFile();

        clearKits();

        f.getKeys(false).stream().forEach(kit ->
        {

            List<MGItem> items = Lists.newArrayList();
            List<PotionEffect> effects = Lists.newArrayList();
            boolean[] isDefault = new boolean[]{true};
            int[] cost = new int[]{0};

            f.getConfigurationSection(kit).getKeys(false).forEach(slot ->
            {
                if (slot.equalsIgnoreCase("potion-effects")) {
                    f.getConfigurationSection(kit + "." + slot).getKeys(false).stream().forEach(effect ->
                    {
                        int amplifier = f.getInt(kit + "." + slot + "." + effect, 0);
                        PotionEffectType type;

                        try {
                            type = PotionEffectType.getByName(effect.toUpperCase());
                        } catch (Exception ignored) {
                            type = PotionEffectType.SATURATION;
                        }

                        PotionEffect potionEffect = new PotionEffect(type, 20 * 25, amplifier);
                        effects.add(potionEffect);
                    });
                } else if (slot.equalsIgnoreCase("default")) {
                    isDefault[0] = f.getBoolean(kit + "." + slot, false);
                } else if (slot.equalsIgnoreCase("cost")) {
                    cost[0] = f.getInt(kit + "." + slot, 0);
                } else {
                    String lore = f.getString(kit + "." + slot + ".lore", "");
                    String name = f.getString(kit + "." + slot + ".name", "");
                    int amount = f.getInt(kit + "." + slot + ".amount", 1);
                    int data = f.getInt(kit + "." + slot + ".data", 0);

                    Map<Enchantment, Integer> enchantments = Maps.newHashMap();

                    if (f.isSet(kit + "." + slot + ".enchantments"))
                    {
                        f.getConfigurationSection(kit + "." + slot + ".enchantments").getKeys(false).stream().forEach(enchantment ->
                        {
                            Enchantment e = Enchantment.getByName(enchantment);
                            int level = f.getInt(kit + "." + slot + ".enchantments." + enchantment);

                            enchantments.put(e, level);
                        });
                    }

                    Material material;
                    try {
                        material = Material.valueOf(f.getString(kit + "." + slot + ".material"));
                    } catch (Exception ignored) {
                        material = Material.POTATO;
                    }

                    MGItem stack = new MGItem(name, Integer.parseInt(slot), material, amount, data, lore, enchantments);
                    items.add(stack);
                }
            });

            register(kit, new Kit(kit, items, effects, isDefault[0], cost[0]));

        });
    }
}
