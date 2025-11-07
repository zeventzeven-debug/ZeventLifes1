package com.zevent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardManager {

    private final LifesPlugin plugin;

    public ScoreboardManager(LifesPlugin plugin) {
        this.plugin = plugin;
    }

    public void createScoreboard(Player player) {
        org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return;

        Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("vidas", "dummy", ChatColor.RED + "❤ Vidas");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        player.setScoreboard(board);
        updateScoreboard(player);
    }

    public void updateScoreboard(Player player) {
        Scoreboard board = player.getScoreboard();
        Objective obj = board.getObjective(DisplaySlot.SIDEBAR);
        if (obj == null) return;

        board.getEntries().forEach(board::resetScores);
        int lives = plugin.getLives(player.getUniqueId());

        Score s1 = obj.getScore(ChatColor.GRAY + "Jugador: " + ChatColor.WHITE + player.getName());
        Score s2 = obj.getScore(ChatColor.RED + "❤ Vidas: " + getColor(lives) + lives);
        s1.setScore(2);
        s2.setScore(1);

        updateTabAndTag(player);
    }

    public void updateTabAndTag(Player player) {
        Scoreboard board = player.getScoreboard();
        Team team = board.getTeam(player.getName());
        if (team == null) {
            team = board.registerNewTeam(player.getName());
            team.addEntry(player.getName());
        }

        int lives = plugin.getLives(player.getUniqueId());
        team.setSuffix(" " + getColor(lives) + "❤" + lives);

        player.setPlayerListName(ChatColor.YELLOW + player.getName() + " " + getColor(lives) + "❤" + lives);
        player.setDisplayName(ChatColor.YELLOW + player.getName() + " " + getColor(lives) + "❤" + lives);
        player.setCustomName(ChatColor.YELLOW + player.getName() + " " + getColor(lives) + "❤" + lives);
        player.setCustomNameVisible(true);

        for (Player online : Bukkit.getOnlinePlayers()) {
            online.setScoreboard(board);
        }
    }

    private ChatColor getColor(int lives) {
        if (lives >= 3) return ChatColor.GREEN;
        if (lives == 2) return ChatColor.YELLOW;
        return ChatColor.RED;
    }
}
