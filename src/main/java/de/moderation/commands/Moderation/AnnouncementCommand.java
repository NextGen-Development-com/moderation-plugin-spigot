package de.moderation.commands.Moderation;

import de.moderation.ModerationPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AnnouncementCommand implements CommandExecutor {

    private final String prefix;
    private final String noPermMsg;
    private final String usageMsg;

    public AnnouncementCommand() {
        prefix = ModerationPlugin.getInstance().getConfig().getString("prefix", "§8[§cModeration§8] §r");
        noPermMsg = prefix + "§cYou do not have permission to use this command.";
        usageMsg = prefix + "§cUsage: /announce <message>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender.hasPermission("moderation.announce") || sender.isOp())) {
            sender.sendMessage(noPermMsg);
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(usageMsg);
            return true;
        }

        String message = String.join(" ", args);
        String announcement = ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n" +
                              ChatColor.GOLD + "" + ChatColor.BOLD + "Announcement:\n" +
                              ChatColor.RESET + "" + ChatColor.WHITE + message + "\n" +
                              ChatColor.DARK_GREEN + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬";

        Bukkit.broadcastMessage(announcement);
        return true;
    }
}