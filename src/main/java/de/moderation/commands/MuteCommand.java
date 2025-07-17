package de.moderation.commands;

import de.moderation.ModerationPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class MuteCommand implements CommandExecutor {
    public static final Set<String> mutedPlayers = new HashSet<>();

    private final String prefix;
    private final String noPermMsg;
    private final String notFoundMsg;
    private final String mutedMsg;
    private final String permNode;

    public MuteCommand(ModerationPlugin plugin) {
        prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix"));
        noPermMsg = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.no-permission").replace("{prefix}", prefix));
        notFoundMsg = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.player-not-found").replace("{prefix}", prefix));
        mutedMsg = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.muted").replace("{prefix}", prefix));
        permNode = plugin.getConfig().getString("permission-nodes.mute");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(permNode)) {
            sender.sendMessage(noPermMsg);
            return true;
        }
        if (args.length < 1) return false;
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage(notFoundMsg);
            return true;
        }
        String reason = args.length >= 2
                ? String.join(" ", args).substring(args[0].length()).trim()
                : "No reason specified";
        mutedPlayers.add(target.getName());
        target.sendMessage(prefix + ChatColor.RED + "You have been muted. Reason: " + reason);
        sender.sendMessage(mutedMsg.replace("{player}", target.getName()).replace("{reason}", reason));
        return true;
    }
}