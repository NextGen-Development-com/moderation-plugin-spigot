package de.moderation.commands.Economy;

import de.moderation.ModerationPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand implements CommandExecutor {
    private final ModerationPlugin plugin;
    private final String prefix;
    private final String noPermMsg;
    private final String usageMsg;

    public PayCommand(ModerationPlugin plugin) {
        this.plugin = plugin;
        prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix"));
        noPermMsg = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.no-permission").replace("{prefix}", prefix));
        usageMsg = prefix + ChatColor.RED + "Usage: /pay <player> <amount>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(prefix + ChatColor.RED + "Only players can use this command.");
            return true;
        }

        if (!player.hasPermission("moderation.pay")) {
            player.sendMessage(noPermMsg);
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(usageMsg);
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            player.sendMessage(prefix + ChatColor.RED + "Player not found.");
            return true;
        }

        if (target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(prefix + ChatColor.RED + "You cannot pay yourself.");
            return true;
        }

        long amount;
        try {
            amount = Long.parseLong(args[1]);
            if (amount <= 0) {
                player.sendMessage(prefix + ChatColor.RED + "Amount must be positive.");
                return true;
            }
        } catch (NumberFormatException e) {
            player.sendMessage(prefix + ChatColor.RED + "Invalid amount.");
            return true;
        }

        if (!plugin.getDatabase().withdraw(player.getUniqueId(), amount)) {
            player.sendMessage(prefix + ChatColor.RED + "You do not have enough money.");
            return true;
        }

        plugin.getDatabase().deposit(target.getUniqueId(), amount);
        player.sendMessage(prefix + ChatColor.GREEN + "You paid " + target.getName() + " " + amount + " coins.");
        target.sendMessage(prefix + ChatColor.GREEN + "You received " + amount + " coins from " + player.getName() + ".");
        return true;
    }
}