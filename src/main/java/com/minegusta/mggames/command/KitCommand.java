package com.minegusta.mggames.command;

import com.google.common.collect.Lists;
import com.minegusta.mggames.config.ConfigManager;
import com.minegusta.mggames.kits.KitRegistry;
import com.minegusta.mggames.kits.MGItem;
import com.minegusta.mggames.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KitCommand implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

        if(!(s instanceof Player) || !s.isOp())return true;

        Player p = (Player) s;

        if(args.length < 2)
        {
            ChatUtil.sendFormattedMessage(p, "Use it like this:", "/kit add <name>", "/kit remove <name>");
            return true;
        }

        FileConfiguration f = ConfigManager.getKitFile();

        if(args[0].equalsIgnoreCase("remove"))
        {
            String kitToRemove = args[1];

            if(KitRegistry.exists(kitToRemove))
            {
                ChatUtil.sendFormattedMessage(p, "You removed kit " + kitToRemove + ".");
                f.set(kitToRemove, null);
                ConfigManager.saveKitFile(f);
                KitRegistry.loadKits();
                return true;
            }
        }
        else if(args[0].equalsIgnoreCase("add"))
        {
            String kitName = args[1];
            List<PotionEffect> effects = Lists.newArrayList();
            List<MGItem> items = Lists.newArrayList();

            for(int i = 0; i < p.getInventory().getContents().length; i++)
            {
                ItemStack pick = p.getInventory().getItem(i);
                if(pick != null && pick.getType() != Material.AIR)
                {
                    String name = pick.getType().name();
                    String lore = "";

                    try
                    {
                        String nameNew = pick.getItemMeta().getDisplayName();
                        if(nameNew != null) name = nameNew;
                        lore = pick.getItemMeta().getLore().get(0);
                    } catch (Exception ignored){}

                    items.add(new MGItem(name, i, pick.getType(), pick.getAmount(), pick.getDurability(), lore));
                }
            }

            items.add(new MGItem(p.getInventory().getHelmet().getType().name(), 103, p.getInventory().getHelmet().getType(), 1, p.getInventory().getHelmet().getDurability(), ""));
            items.add(new MGItem(p.getInventory().getBoots().getType().name(), 100, p.getInventory().getBoots().getType(), 1, p.getInventory().getBoots().getDurability(), ""));
            items.add(new MGItem(p.getInventory().getChestplate().getType().name(), 102, p.getInventory().getChestplate().getType(), 1, p.getInventory().getChestplate().getDurability(), ""));
            items.add(new MGItem(p.getInventory().getLeggings().getType().name(), 101, p.getInventory().getLeggings().getType(), 1, p.getInventory().getLeggings().getDurability(), ""));

            effects.addAll(p.getActivePotionEffects().stream().collect(Collectors.toList()));


            ChatUtil.sendFormattedMessage(p, "You added kit " + kitName + ".");

            //Add the entries
            items.stream().forEach(i ->
                    {
                        String slot = Integer.toString(i.getSlot());
                        f.set(kitName + "." + slot + ".amount", i.getAmount());
                        f.set(kitName + "." + slot + ".name", ChatColor.stripColor(i.getName()));
                        f.set(kitName + "." + slot + ".material", i.getMaterial().name());
                        f.set(kitName + "." + slot + ".lore", ChatColor.stripColor(i.getLore()));
                        f.set(kitName + "." + slot + ".data", i.getData());
                    });
            effects.stream().forEach(effect ->
                    f.set(kitName + "." + "potion-effects." + effect.getType().getName(), effect.getAmplifier()));


            ConfigManager.saveKitFile(f);
            KitRegistry.loadKits();
            return true;
        }
        return true;
    }
}
