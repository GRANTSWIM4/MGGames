package com.minegusta.mggames.config;

import com.minegusta.mggames.game.GameTypes;
import com.minegusta.mggames.main.Main;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager
{
    public static final String fileName = "games.yml";
    public static final String path = "/games/";

    public static void loadSessions()
    {
        FileConfiguration conf = YamlUtil.getConfiguration(path,fileName);
        for(String s : conf.getKeys(false))
        {
            GameTypes type;
            try {
                type = GameTypes.valueOf(conf.getString(s + ".type").toUpperCase());
            } catch (Exception ignored){continue;}
            type.createSession(s, conf.getConfigurationSection(s));
        }
    }

    public static FileConfiguration getGameConfig()
    {
        return YamlUtil.getConfiguration(path,fileName);
    }

    public static void saveGameConfig(FileConfiguration f)
    {
        YamlUtil.saveFile(path,fileName, f);
    }

    public static FileConfiguration getKitFile()
    {
        return YamlUtil.getConfiguration("/kits/", "kits.yml");
    }

    public static void saveKitFile(FileConfiguration f)
    {
        YamlUtil.saveFile("/kits/", "kits.yml", f);
    }

    public static void loadDefaultConfig()
    {
        Main.getPlugin().saveDefaultConfig();
    }

    public static FileConfiguration getDefaultConfig()
    {
        return Main.getPlugin().getConfig();
    }
}
