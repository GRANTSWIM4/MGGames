package com.minegusta.mggames.command;

import com.google.common.collect.Lists;
import com.minegusta.mggames.config.ConfigManager;
import com.minegusta.mggames.kits.KitRegistry;
import com.minegusta.mggames.kits.MGItem;
import com.minegusta.mggames.util.ChatUtil;
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
                    items.add(new MGItem(pick.getItemMeta().getDisplayName(), i, pick.getType(), pick.getAmount(), pick.getDurability(), pick.getItemMeta().getLore().get(0)));
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

                        ConfigurationSection section = f.getConfigurationSection(kitName + "." + i.getName());

                        section.set("amount", i.getAmount());
                        section.set("slot", i.getSlot());
                        section.set("material", i.getMaterial());
                        section.set("lore", i.getLore());
                        section.set("data", i.getData());
                    });
            effects.stream().forEach(effect ->
            {
                ConfigurationSection section = f.getConfigurationSection(kitName + "." + "potion-effects");
                section.set(effect.getType().getName(), effect.getAmplifier());
            });


            ConfigManager.saveKitFile(f);
            KitRegistry.loadKits();
            return true;
        }
        return true;
    }
}
