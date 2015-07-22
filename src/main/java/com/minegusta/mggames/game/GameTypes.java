package com.minegusta.mggames.game;

import com.minegusta.mggames.gametype.CaptureTheFlag;
import com.minegusta.mggames.register.Register;
import org.bukkit.configuration.ConfigurationSection;

public enum GameTypes
{
    CTF(new CaptureTheFlag());

    private AbstractGame game;

    GameTypes(AbstractGame game)
    {
        this.game = game;
    }

    public void createSession(String name, ConfigurationSection f)
    {
        Register.registerGame(name, game.insertData(f));
    }
}
