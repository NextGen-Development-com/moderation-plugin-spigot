package de.moderation.commands.Moderation;

import de.moderation.ModerationPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class VanishCommand implements CommandExecutor {

    private final String prefix;
    private final String noPermMsg;
    private final String usageMsg;
    private final ModerationPlugin plugin;

    public VanishCommand(ModerationPlugin plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getConfig().getString("prefix", "§8[§cModeration§8] §r");
        this.noPermMsg = prefix + "§cYou do not have permission to use this command.";
        this.usageMsg = prefix + "§cUsage: /vanish [player]";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("moderation.vanish")) {
            sender.sendMessage(noPermMsg);
            return true;
        }

        if (args.length == 0) {
            if (!(sender instanceof Player p)) {
                sender.sendMessage(prefix + "§cOnly players can use this command without arguments.");
                return true;
            }
            toggleVanish(p);
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(prefix + "§cPlayer not found.");
                return true;
            }
            toggleVanish(target);
            sender.sendMessage(prefix + "§aToggled vanish for " + target.getName() + ".");
            return true;
        }

        sender.sendMessage(usageMsg);
        return true;
    }

    private void toggleVanish(Player p) {
        boolean vanished = p.hasMetadata("vanished");

        if (!vanished) {
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (!online.equals(p)) online.hidePlayer(plugin, p);
            }
            p.setMetadata("vanished", new FixedMetadataValue(plugin, true));
            p.sendMessage(prefix + "§aYou are now vanished.");
        } else {
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.showPlayer(plugin, p);
            }
            p.removeMetadata("vanished", plugin);
            p.sendMessage(prefix + "§aYou are no longer vanished.");
        }
    }
}