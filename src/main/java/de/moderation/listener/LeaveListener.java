package de.moderation.listener;

import de.moderation.ModerationPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    private final String prefix;

    public LeaveListener() {
        prefix = ChatColor.translateAlternateColorCodes('&',
            ModerationPlugin.getInstance().getConfig().getString("prefix", "&8[&cModeration&8] &r")
        );
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        String playerName = ChatColor.BLUE + event.getPlayer().getName() + ChatColor.RESET;
        int onlineCount = Bukkit.getOnlinePlayers().size() - 1;

        String message = prefix + playerName +
                " left the server. Now " +
                ChatColor.GREEN + onlineCount + ChatColor.RESET + " members online.";

        Bukkit.broadcastMessage(message);
    }
}