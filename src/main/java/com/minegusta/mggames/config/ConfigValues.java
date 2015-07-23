package com.minegusta.mggames.config;

public enum ConfigValues
{
    RED_WOOL("red-wool"),
    BLUE_WOOL("blue-wool"),
    SPECTATOR_SPAWN("spectator-spawn"),
    TEAM_RED_SPAWN("team-red-spawn"),
    TEAM_BLUE_SPAWN("team-blue-spawn"),
    TEAM_FFA_SPAWNS("team-ffa-spawns"),
    FLAG_LIVES("flag-lives"),
    LOBBY_LOCATION("lobby-location"),
    MAX_PLAYERS("max-players"),
    MIN_PLAYERS("min-players"),
    GRACE_TIME("grace-time"),
    NAME("name"),
    MAX_PLAYTIME("max-time"),
    RESPAWN_WITH_KIT("respawn-with-kit"),
    TYPE("type"),
    WINNER_COMMANDS("winner-commands"),
    LOSER_COMMANDS("loser-commands"),
    TIE_COMMANDS("tie-commands"),
    FORCE_KIT("force-kit"),
    DEFAULT_KIT("default-kit"),
    AVAILABLE_KITS("kits"),
    TICKETS("tickets"),
    UNLOCKABLES("unlockables"),
    ALLOW_BUILD("build-blocks"),
    ALLOW_BREAK("break-blocks"),
    WORLDS("worlds");




    private String path;

    private ConfigValues(String path)
    {
        this.path = path;
    }

    public String getPath()
    {
        return path;
    }
}
