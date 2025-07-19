package de.moderation.commands.Utility;

import de.moderation.ModerationPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CraftCommand implements CommandExecutor {

    private final String prefix;
    private final String noPermMsg;

    public CraftCommand() {
        prefix = ModerationPlugin.getInstance().getConfig().getString("prefix", "§8[§cModeration§8] §r");
        noPermMsg = prefix + "§cYou do not have permission to use this command.";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(prefix + "§cOnly players can use this command.");
            return true;
        }

        if (!p.hasPermission("moderation.craft")) {
            p.sendMessage(noPermMsg);
            return true;
        }

        p.openWorkbench(null, true);
        p.sendMessage(prefix + "§aCrafting table opened.");
        return true;
    }
}