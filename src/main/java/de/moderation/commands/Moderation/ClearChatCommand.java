package de.moderation.commands.Moderation;

import de.moderation.ModerationPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChatCommand implements CommandExecutor {
    private final String prefix;
    private final String noPermMsg;
    private final String onlyPlayersMsg;

    public ClearChatCommand() {
        prefix = ModerationPlugin.getInstance().getConfig().getString("prefix", "§8[§cModeration§8] §r");
        noPermMsg = prefix + "§cYou don't have permission to use this command.";
        onlyPlayersMsg = prefix + "§cOnly players can use this command.";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(onlyPlayersMsg);
            return true;
        }

        if (!p.isOp() && !p.hasPermission("moderation.clearchat")) {
            p.sendMessage(noPermMsg);
            return true;
        }

        for (int i = 0; i < 100; i++) {
            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(""));
        }

        Bukkit.getOnlinePlayers().forEach(player ->
                player.sendMessage(prefix + "§aThe chat was cleared by §2" + p.getName() + "§a.")
        );

        return true;
    }
}