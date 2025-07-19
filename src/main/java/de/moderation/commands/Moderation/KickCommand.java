package de.moderation.commands.Moderation;

import de.moderation.ModerationPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class KickCommand implements CommandExecutor {
    private final ModerationPlugin plugin;
    private final String prefix;
    private final String noPermMsg;
    private final String notFoundMsg;
    private final String kickedMsg;
    private final String permNode;

    public KickCommand(ModerationPlugin plugin) {
        this.plugin = plugin;
        this.prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix"));
        this.noPermMsg = plugin.getConfig().getString("messages.no-permission").replace("{prefix}", prefix);
        this.notFoundMsg = plugin.getConfig().getString("messages.player-not-found").replace("{prefix}", prefix);
        this.kickedMsg = plugin.getConfig().getString("messages.kicked").replace("{prefix}", prefix);
        this.permNode = plugin.getConfig().getString("permission-nodes.kick");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(permNode)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermMsg));
            return true;
        }
        if (args.length < 1) {
            return false;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', notFoundMsg));
            return true;
        }
        String reason = args.length >= 2
                ? String.join(" ", args).substring(args[0].length()).trim()
                : "No reason specified";
        target.kickPlayer(prefix + ChatColor.RED + "Kicked: " + reason);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                kickedMsg
                        .replace("{player}", target.getName())
                        .replace("{reason}", reason)
        ));
        return true;
    }
}