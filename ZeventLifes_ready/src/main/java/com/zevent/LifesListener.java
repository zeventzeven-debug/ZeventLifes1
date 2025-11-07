package com.zevent;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Date;

public class LifesListener implements Listener {

    private final LifesPlugin plugin;

    public LifesListener(LifesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (plugin.getLives(player.getUniqueId()) == 0) {
            plugin.setLives(player.getUniqueId(), 3);
        }
        plugin.getScoreboardManager().createScoreboard(player);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        int lives = plugin.getLives(player.getUniqueId()) - 1;
        plugin.setLives(player.getUniqueId(), Math.max(lives, 0));

        World world = player.getWorld();
        world.strikeLightningEffect(player.getLocation());

        for (Player online : Bukkit.getOnlinePlayers()) {
            online.playSound(online.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 5.0f, 1.0f);
        }

        if (lives <= 0) {
            Bukkit.getBanList(BanList.Type.NAME).addBan(
                    player.getName(),
                    ChatColor.RED + "Has perdido tus 3 vidas. ¡Estás baneado del servidor!",
                    null,
                    "Sistema de vidas"
            );
            player.kickPlayer(ChatColor.RED + "Has perdido tus 3 vidas. ¡Estás baneado del servidor!");
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "⚡ " + player.getName() + " ha perdido sus 3 vidas y fue baneado del servidor.");
        } else {
            player.sendMessage(ChatColor.RED + "⚡ Has perdido una vida. Te quedan " + lives + " vidas.");
            Bukkit.broadcastMessage(ChatColor.YELLOW + "⚡ " + player.getName() + " ha perdido una vida. (" + ChatColor.RED + lives + "❤" + ChatColor.YELLOW + " restantes)");
            plugin.getScoreboardManager().updateScoreboard(player);
        }
    }
}
