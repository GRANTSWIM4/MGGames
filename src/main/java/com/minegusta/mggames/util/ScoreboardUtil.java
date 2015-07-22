package com.minegusta.mggames.util;

import com.minegusta.mggames.game.TeamType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardUtil {
    private static Scoreboard board;
    private static Objective objective;

    public static void setBoard() {
        board = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = board.registerNewObjective("teamboard", "health");
        objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        objective.setDisplayName(ChatColor.RED + "Health");

        for (TeamType type : TeamType.values()) {
            Team team = board.registerNewTeam(type.name());
            team.setPrefix(type.getColor() + "");
            team.setDisplayName(type.getColor() + "");
            team.setNameTagVisibility(NameTagVisibility.ALWAYS);
            team.setCanSeeFriendlyInvisibles(true);
        }
    }

    public static void addScoreBoard(Player p, TeamType teamType) {
        Team team = board.getTeam(teamType.name());
        team.addPlayer(p);
        p.setScoreboard(board);
    }

    public static void removeScoreBoard(Player p) {
        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }
}
