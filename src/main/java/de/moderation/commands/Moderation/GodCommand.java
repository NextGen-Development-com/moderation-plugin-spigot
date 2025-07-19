package de.moderation.commands.Moderation;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GodCommand implements CommandExecutor, Listener {

    private final Set<UUID> godmodePlayers = new HashSet<>();
    private final String prefix;
    private final String noPermMsg;
    private final String playerOnlyMsg;
    private final String notFoundMsg;

    public GodCommand(JavaPlugin plugin) {
        this.prefix = plugin.getConfig().getString("prefix", "§8[§cModeration§8] §r");
        this.noPermMsg = prefix + "§cYou do not have permission to use this command.";
        this.playerOnlyMsg = prefix + "§cOnly players can use this command.";
        this.notFoundMsg = prefix + "§cPlayer not found.";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(playerOnlyMsg);
            return true;
        }

        Player p = (Player) sender;

        if (args.length == 0) {
            if (!p.hasPermission("moderation.god")) {
                p.sendMessage(noPermMsg);
                return true;
            }

            toggleGod(p, p);
            return true;
        }

        if (!p.hasPermission("moderation.god")) {
            p.sendMessage(noPermMsg);
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            p.sendMessage(notFoundMsg);
            return true;
        }

        toggleGod(p, target);
        return true;
    }

    private void toggleGod(Player sender, Player target) {
        UUID uuid = target.getUniqueId();
        if (godmodePlayers.contains(uuid)) {
            godmodePlayers.remove(uuid);
            target.sendMessage(prefix + "§cGod mode disabled.");
            if (!sender.equals(target))
                sender.sendMessage(prefix + "§cGod mode disabled for §9" + target.getName() + "§c.");
        } else {
            godmodePlayers.add(uuid);
            target.sendMessage(prefix + "§aGod mode enabled.");
            if (!sender.equals(target))
                sender.sendMessage(prefix + "§aGod mode enabled for §9" + target.getName() + "§a.");
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player p && godmodePlayers.contains(p.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}