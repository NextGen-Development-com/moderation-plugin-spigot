package de.moderation.commands;

import de.moderation.ModerationPlugin;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {
    private final String prefix;
    private final String noPermMsgRaw;
    private final String usageMsgRaw;
    private final String gmChangedMsgRaw;
    private final String permNode;

    public GamemodeCommand(ModerationPlugin plugin) {
        prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix"));
        noPermMsgRaw = plugin.getConfig().getString("messages.no-permission");
        usageMsgRaw = plugin.getConfig().getString("messages.gm-usage", "{prefix}&cUsage: /gm <0|1|2|3>");
        gmChangedMsgRaw = plugin.getConfig().getString("messages.gm-changed", "{prefix}&aYour game mode has been set to &e{mode}&a.");
        permNode = plugin.getConfig().getString("permission-nodes.gamemode", "moderation.gamemode");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(prefix + ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        if (!player.hasPermission(permNode)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermMsgRaw.replace("{prefix}", prefix)));
            return true;
        }
        if (args.length != 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', usageMsgRaw.replace("{prefix}", prefix)));
            return true;
        }
        GameMode mode;
        switch (args[0]) {
            case "0": mode = GameMode.SURVIVAL; break;
            case "1": mode = GameMode.CREATIVE; break;
            case "2": mode = GameMode.ADVENTURE; break;
            case "3": mode = GameMode.SPECTATOR; break;
            default:
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', usageMsgRaw.replace("{prefix}", prefix)));
                return true;
        }
        player.setGameMode(mode);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', gmChangedMsgRaw.replace("{prefix}", prefix).replace("{mode}", mode.name())));
        return true;
    }
}