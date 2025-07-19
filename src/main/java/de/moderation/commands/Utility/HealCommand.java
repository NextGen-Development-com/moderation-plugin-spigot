package de.moderation.commands.Utility;

import de.moderation.ModerationPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {
    private final String prefix;
    private final String noPermSelf;
    private final String noPermOthers;

    public HealCommand() {
        prefix = ModerationPlugin.getInstance().getConfig().getString("prefix", "§8[§cModeration§8] §r");
        noPermSelf = prefix + "§cYou don’t have permission to heal yourself.";
        noPermOthers = prefix + "§cYou don’t have permission to heal others.";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player p)) {
                sender.sendMessage(prefix + "§cOnly players can heal themselves.");
                return true;
            }
            if (!p.hasPermission("moderation.heal")) {
                p.sendMessage(noPermSelf);
                return true;
            }
            p.setHealth(p.getMaxHealth());
            p.sendMessage(prefix + "§aYou have been healed.");
            return true;
        } else if (args.length == 1) {
            if (!sender.hasPermission("moderation.heal")) {
                sender.sendMessage(noPermOthers);
                return true;
            }
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(prefix + "§cPlayer not found.");
                return true;
            }
            target.setHealth(target.getMaxHealth());
            target.sendMessage(prefix + "§aYou have been healed.");
            sender.sendMessage(prefix + "§aYou healed " + target.getName() + ".");
            return true;
        } else {
            sender.sendMessage(prefix + "§cUsage: /heal [player]");
            return true;
        }
    }
}