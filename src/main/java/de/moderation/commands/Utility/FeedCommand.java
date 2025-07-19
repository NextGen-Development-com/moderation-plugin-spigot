package de.moderation.commands.Utility;

import de.moderation.ModerationPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCommand implements CommandExecutor {
    private final String prefix;
    private final String noPermSelf;
    private final String noPermOthers;

    public FeedCommand() {
        prefix = ModerationPlugin.getInstance().getConfig().getString("prefix", "§8[§cModeration§8] §r");
        noPermSelf = prefix + "§cYou don’t have permission to feed yourself.";
        noPermOthers = prefix + "§cYou don’t have permission to feed others.";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player p)) {
                sender.sendMessage(prefix + "§cOnly players can feed themselves.");
                return true;
            }
            if (!p.hasPermission("moderation.feed")) {
                p.sendMessage(noPermSelf);
                return true;
            }
            p.setFoodLevel(20);
            p.setSaturation(20f);
            p.sendMessage(prefix + "§aYou have been fully fed and your saturation is maxed.");
            p.sendMessage(prefix + "§7Stay healthy and enjoy your game!");
            return true;
        } else if (args.length == 1) {
            if (!sender.hasPermission("moderation.feed")) {
                sender.sendMessage(noPermOthers);
                return true;
            }
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(prefix + "§cPlayer not found.");
                return true;
            }
            target.setFoodLevel(20);
            target.setSaturation(20f);
            target.sendMessage(prefix + "§aYou have been fully fed and your saturation is maxed.");
            target.sendMessage(prefix + "§7Stay healthy and enjoy your game!");
            sender.sendMessage(prefix + "§aYou fed " + target.getName() + ".");
            return true;
        } else {
            sender.sendMessage(prefix + "§cUsage: /feed [player]");
            return true;
        }
    }
}