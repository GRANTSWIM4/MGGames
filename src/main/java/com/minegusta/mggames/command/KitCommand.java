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

import java.util.List;

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

            for(int i = 0; i < p.getInventory().getSize(); i++)
            {
                ItemStack pick = p.getInventory().getItem(i);
                if(pick != null && pick.getType() != Material.AIR)
                {
                    String name = pick.getType().name();
                    String lore = "";

                    try
                    {
                        name = pick.getItemMeta().getDisplayName();
                        lore = pick.getItemMeta().getLore().get(0);
                    } catch (Exception ignored){}

                    items.add(new MGItem(name, i, pick.getType(), pick.getAmount(), pick.getDurability(), lore));
                }
            }

            for(PotionEffect effect : p.getActivePotionEffects())
            {
                effects.add(effect);
            }


            ChatUtil.sendFormattedMessage(p, "You added kit " + kitName + ".");

            //Add the entries
            items.stream().forEach(i ->
                    {
                        String name = ChatColor.stripColor(i.getName());
                        f.set(kitName + "." + name + ".amount", i.getAmount());
                        f.set(kitName + "." + name + ".slot", i.getSlot());
                        f.set(kitName + "." + name + ".material", ChatColor.stripColor(i.getMaterial().name()));
                        f.set(kitName + "." + name + ".lore", ChatColor.stripColor(i.getLore()));
                        f.set(kitName + "." + name + ".data", i.getData());
                    });
            effects.stream().forEach(effect ->
            {
                f.set(kitName + "." + "potion-effects." + effect.getType().getName(), effect.getAmplifier());
            });


            ConfigManager.saveKitFile(f);
            KitRegistry.loadKits();
            return true;
        }
        return true;
    }
}
