package de.moderation.commands.Utility;

import de.moderation.ModerationPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class EnderChestCommand implements CommandExecutor {

    private final String prefix;
    private final String noPermOwn;
    private final String noPermOthers;

    public EnderChestCommand() {
        prefix = ModerationPlugin.getInstance().getConfig().getString("prefix", "§8[§cModeration§8] §r");
        noPermOwn = prefix + "§cYou do not have permission to open your own Ender Chest.";
        noPermOthers = prefix + "§cYou do not have permission to open others' Ender Chests.";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(prefix + "§cOnly players can use this command.");
            return true;
        }

        if (args.length == 0) {
            if (!p.hasPermission("moderation.ec")) {
                p.sendMessage(noPermOwn);
                return true;
            }
            p.openInventory(p.getEnderChest());
            p.sendMessage(prefix + "§aYour Ender Chest has been opened.");
            return true;
        }

        if (args.length == 1) {
            if (!p.hasPermission("moderation.ec.others")) {
                p.sendMessage(noPermOthers);
                return true;
            }
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                p.sendMessage(prefix + "§cPlayer not found.");
                return true;
            }
            p.openInventory(target.getEnderChest());
            p.sendMessage(prefix + "§aOpened Ender Chest of " + target.getName() + ".");
            return true;
        }

        p.sendMessage(prefix + "§cUsage: /ec [player]");
        return true;
    }
}