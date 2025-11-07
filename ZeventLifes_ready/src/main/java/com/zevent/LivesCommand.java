package com.zevent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity<Player;

public class LivesCommand implements CommandExecutor {

    private final LifesPlugin plugin;

    public LivesCommand(LifesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("vidas.admin")) {
            sender.sendMessage(ChatColor.RED + "No tienes permiso para usar este comando.");
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Jugador no encontrado.");
                return true;
            }
            int v = plugin.getLives(target.getUniqueId());
            sender.sendMessage(ChatColor.YELLOW + target.getName() + " tiene " + ChatColor.RED + v + ChatColor.YELLOW + " vidas.");
            return true;
        }

        if (args.length >= 3) {
            String action = args[0].toLowerCase();
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Jugador no encontrado.");
                return true;
            }

            int amount;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "La cantidad debe ser un número.");
                return true;
            }

            int current = plugin.getLives(target.getUniqueId());

            switch (action) {
                case "set":
                    plugin.setLives(target.getUniqueId(), Math.max(0, amount));
                    sender.sendMessage(ChatColor.GREEN + "Se establecieron " + amount + " vidas a " + target.getName());
                    break;
                case "add":
                    plugin.setLives(target.getUniqueId(), current + amount);
                    sender.sendMessage(ChatColor.GREEN + "Se añadieron " + amount + " vidas a " + target.getName());
                    break;
                case "remove":
                    plugin.setLives(target.getUniqueId(), Math.max(0, current - amount));
                    sender.sendMessage(ChatColor.GREEN + "Se quitaron " + amount + " vidas a " + target.getName());
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Uso: /vidas <set|add|remove> <jugador> <cantidad>");
                    return true;
            }

            plugin.getScoreboardManager().updateScoreboard(target);
            return true;
        }

        sender.sendMessage(ChatColor.YELLOW + "Uso:");
        sender.sendMessage(ChatColor.GRAY + " /vidas <jugador>");
        sender.sendMessage(ChatColor.GRAY + " /vidas set <jugador> <cantidad>");
        sender.sendMessage(ChatColor.GRAY + " /vidas add <jugador> <cantidad>");
        sender.sendMessage(ChatColor.GRAY + " /vidas remove <jugador> <cantidad>");
        return true;
    }
}
