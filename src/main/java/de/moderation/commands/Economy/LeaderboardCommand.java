package de.moderation.commands.Economy;

import de.moderation.ModerationPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardCommand implements CommandExecutor {
    private final ModerationPlugin plugin;
    private final String prefix;
    private final String noPermMsg;

    public LeaderboardCommand(ModerationPlugin plugin) {
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

        if (!player.hasPermission("moderation.leaderboard")) {
            player.sendMessage(noPermMsg);
            return true;
        }

        List<Entry> topList = new ArrayList<>();

        try (Connection conn = plugin.getDatabase().getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT uuid, balance FROM player_money ORDER BY balance DESC LIMIT 10"
             );
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String uuid = rs.getString("uuid");
                long balance = rs.getLong("balance");
                topList.add(new Entry(uuid, balance));
            }
        } catch (SQLException e) {
            player.sendMessage(prefix + ChatColor.RED + "Error loading leaderboard.");
            return true;
        }

        player.sendMessage(prefix + ChatColor.GOLD + " Top 10 Richest Players:");
        player.sendMessage(ChatColor.GRAY + "---------------------------------");
        int rank = 1;
        for (Entry entry : topList) {
            String name = plugin.getServer().getOfflinePlayer(java.util.UUID.fromString(entry.uuid)).getName();
            if (name == null) name = "Unknown";

            player.sendMessage(ChatColor.YELLOW + "#" + rank + " " + ChatColor.GREEN + name + ChatColor.WHITE + " - " + ChatColor.GOLD + entry.balance);
            rank++;
        }
        player.sendMessage(ChatColor.GRAY + "---------------------------------");
        return true;
    }

    private static class Entry {
        String uuid;
        long balance;

        Entry(String uuid, long balance) {
            this.uuid = uuid;
            this.balance = balance;
        }
    }
}