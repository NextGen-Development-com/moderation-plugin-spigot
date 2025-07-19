package de.moderation.commands.Moderation;

import de.moderation.ModerationPlugin;
import de.moderation.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand implements CommandExecutor {

    private final String prefix;
    private final String noPermMsg;
    private final String notFoundMsg;
    private final String mutedMsg;
    private final String permNode;
    private final Database database;

    public MuteCommand(ModerationPlugin plugin) {
        this.database = plugin.getDatabase();
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

        database.mutePlayer(target.getUniqueId(), reason);
        target.sendMessage(prefix + ChatColor.RED + "You have been muted. Reason: " + reason);
        sender.sendMessage(mutedMsg.replace("{player}", target.getName()).replace("{reason}", reason));
        return true;
    }
}