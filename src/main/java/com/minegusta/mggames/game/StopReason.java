package com.minegusta.mggames.game;

public enum StopReason
{
    WINNER("We have a winner!"),
    RELOAD("All games are being reloaded!"),
    TIME_UP("Game time ran out!"),
    WORLD_ERROR("There was an error starting your game.");

    private String message;

    StopReason(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }


}
