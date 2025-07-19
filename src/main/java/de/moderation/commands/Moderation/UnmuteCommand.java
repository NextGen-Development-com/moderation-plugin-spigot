package de.moderation.commands.Moderation;

import de.moderation.ModerationPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnmuteCommand implements CommandExecutor {
    private final String prefix;
    private final String noPermMsg;
    private final String notFoundMsg;
    private final String notMutedMsg;
    private final String unmutedMsg;
    private final String permNode;

    public UnmuteCommand(ModerationPlugin plugin) {
        prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix"));
        noPermMsg = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.no-permission").replace("{prefix}", prefix));
        notFoundMsg = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.player-not-found").replace("{prefix}", prefix));
        notMutedMsg = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.not-muted", "{prefix}&c{player} is not muted.").replace("{prefix}", prefix));
        unmutedMsg = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.unmuted", "{prefix}&aUnmuted &e{player}&a.").replace("{prefix}", prefix));
        permNode = plugin.getConfig().getString("permission-nodes.unmute", "moderation.unmute");
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

        if (!ModerationPlugin.getInstance().getDatabase().isMuted(target.getUniqueId())) {
            sender.sendMessage(notMutedMsg.replace("{player}", target.getName()));
            return true;
        }

        ModerationPlugin.getInstance().getDatabase().unmutePlayer(target.getUniqueId());

        sender.sendMessage(unmutedMsg.replace("{player}", target.getName()));
        target.sendMessage(prefix + ChatColor.GREEN + "You have been unmuted.");

        return true;
    }
}