package de.moderation.commands.Economy;

import de.moderation.ModerationPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EcoCommand implements CommandExecutor {
    private final ModerationPlugin plugin;
    private final String prefix;
    private final String noPermMsg;
    private final String invalidUsageMsg;
    private final String notFoundMsg;
    private final String permNodeSet;
    private final String permNodeTake;

    public EcoCommand(ModerationPlugin plugin) {
        this.plugin = plugin;
        prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix"));
        noPermMsg = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.no-permission").replace("{prefix}", prefix));
        invalidUsageMsg = prefix + ChatColor.RED + " Usage: /eco <set|take> <player> <amount>";
        notFoundMsg = prefix + ChatColor.RED + " Player not found.";
        permNodeSet = "moderation.eco.set";
        permNodeTake = "moderation.eco.take";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(invalidUsageMsg);
            return true;
        }

        String action = args[0].toLowerCase();
        String playerName = args[1];
        String amountStr = args[2];

        Player target = Bukkit.getPlayerExact(playerName);
        if (target == null) {
            sender.sendMessage(notFoundMsg);
            return true;
        }

        UUID uuid = target.getUniqueId();

        long amount;
        try {
            amount = Long.parseLong(amountStr);
            if (amount < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            sender.sendMessage(prefix + ChatColor.RED + " Invalid amount.");
            return true;
        }

        switch (action) {
            case "set" -> {
                if (!sender.hasPermission(permNodeSet)) {
                    sender.sendMessage(noPermMsg);
                    return true;
                }
                plugin.getDatabase().setBalance(uuid, amount);
                sender.sendMessage(prefix + ChatColor.GREEN + " Set balance of " + target.getName() + " to " + amount + ".");
                target.sendMessage(prefix + ChatColor.GREEN + " Your balance has been set to " + amount + ".");
            }
            case "take" -> {
                if (!sender.hasPermission(permNodeTake)) {
                    sender.sendMessage(noPermMsg);
                    return true;
                }
                long current = plugin.getDatabase().getBalance(uuid);
                if (current < amount) {
                    sender.sendMessage(prefix + ChatColor.RED + " Player only has " + current + " coins.");
                    return true;
                }
                plugin.getDatabase().withdraw(uuid, amount);
                sender.sendMessage(prefix + ChatColor.GREEN + " Took " + amount + " from " + target.getName() + ".");
                target.sendMessage(prefix + ChatColor.RED + " " + amount + " coins have been taken from your balance.");
            }
            default -> sender.sendMessage(invalidUsageMsg);
        }
        return true;
    }
}