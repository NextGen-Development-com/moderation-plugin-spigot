package de.moderation.commands.Economy;

import de.moderation.ModerationPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {
    private final ModerationPlugin plugin;
    private final String prefix;
    private final String noPermMsg;

    public BalanceCommand(ModerationPlugin plugin) {
        this.plugin = plugin;
        prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix"));
        noPermMsg = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.no-permission").replace("{prefix}", prefix));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(prefix + ChatColor.RED + "Only players can use this command.");
            return true;
        }

        if (!player.hasPermission("moderation.balance")) {
            player.sendMessage(noPermMsg);
            return true;
        }

        long balance = plugin.getDatabase().getBalance(player.getUniqueId());
        player.sendMessage(prefix + ChatColor.GOLD + "Your balance: " + ChatColor.GREEN + balance + " coins");
        return true;
    }
}