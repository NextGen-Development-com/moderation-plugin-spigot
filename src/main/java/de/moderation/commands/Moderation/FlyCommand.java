package de.moderation.commands.Moderation;

import de.moderation.ModerationPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {
    private final String prefix;
    private final String noPermMsgRaw;
    private final String enabledMsgRaw;
    private final String disabledMsgRaw;
    private final String permNode;

    public FlyCommand(ModerationPlugin plugin) {
        prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix"));
        noPermMsgRaw = plugin.getConfig().getString("messages.no-permission");
        enabledMsgRaw = plugin.getConfig().getString("messages.fly-enabled", "{prefix}&aFlight enabled.");
        disabledMsgRaw = plugin.getConfig().getString("messages.fly-disabled", "{prefix}&cFlight disabled.");
        permNode = plugin.getConfig().getString("permission-nodes.fly", "moderation.fly");
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
        boolean flyEnabled = player.getAllowFlight();
        player.setAllowFlight(!flyEnabled);
        player.setFlying(!flyEnabled);
        String msg = flyEnabled ? disabledMsgRaw : enabledMsgRaw;
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg.replace("{prefix}", prefix)));
        return true;
    }
}