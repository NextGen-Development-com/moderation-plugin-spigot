package de.moderation.commands.Utility;

import de.moderation.ModerationPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class HelpCommand implements CommandExecutor {
    private final ModerationPlugin plugin;
    private final String prefix;
    
    public HelpCommand(ModerationPlugin plugin) {
        this.plugin = plugin;
        this.prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

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
              if (usage.startsWith("/" + cmdName)) {
                usage = usage.substring(cmdName.length() + 1).trim();
            }

            String usageDisplay = usage.isEmpty() ? "" : " " + ChatColor.GRAY + usage;

            sender.sendMessage(ChatColor.YELLOW + "/" + cmdName + usageDisplay + ChatColor.RESET + " - " + ChatColor.WHITE + description);
        }

        sender.sendMessage(ChatColor.GRAY + "-----------------------------------------");
        sender.sendMessage(ChatColor.DARK_GRAY + "Plugin by " + ChatColor.RED + "NextGen Development");
        sender.sendMessage(ChatColor.DARK_GRAY + "Discord: " + ChatColor.GREEN + "https://discord.gg/zQRYbaXgNa");

        return true;
    }
}