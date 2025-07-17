package de.moderation.commands;

import de.moderation.ModerationPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class HelpCommand implements CommandExecutor {
    private final ModerationPlugin plugin;
    private final String prefix;
    private final String noPermMsg;
    private final String permNode;

    public HelpCommand(ModerationPlugin plugin) {
        this.plugin = plugin;
        this.prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix"));
        this.noPermMsg = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.no-permission").replace("{prefix}", prefix));
        this.permNode = plugin.getConfig().getString("permission-nodes.help", "moderation.help");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(permNode)) {
            sender.sendMessage(noPermMsg);
            return true;
        }

        sender.sendMessage(prefix + ChatColor.GOLD + " Moderation Plugin Commands:");
        sender.sendMessage(ChatColor.GRAY + "-----------------------------------------");

        Map<String, Map<String, Object>> commands = plugin.getDescription().getCommands();

        if (commands == null) {
            sender.sendMessage(ChatColor.RED + "No commands found in plugin.yml");
            return true;
        }

        for (Map.Entry<String, Map<String, Object>> entry : commands.entrySet()) {
            String cmdName = entry.getKey();
            Map<String, Object> details = entry.getValue();

            String description = details.getOrDefault("description", "No description").toString();
            String usage = details.getOrDefault("usage", "").toString();

            String usageDisplay = usage.isEmpty() ? "" : " " + ChatColor.GRAY + usage;

            sender.sendMessage(ChatColor.YELLOW + "/" + cmdName + usageDisplay + ChatColor.RESET + " - " + ChatColor.WHITE + description);
        }

        sender.sendMessage(ChatColor.GRAY + "-----------------------------------------");
        sender.sendMessage(ChatColor.DARK_GRAY + "Plugin by " + ChatColor.RED + "NextGen Development");
        sender.sendMessage(ChatColor.DARK_GRAY + "Discord: " + ChatColor.GREEN + "https://discord.gg/zQRYbaXgNa");

        return true;
    }
}