package de.moderation.listener;

import de.moderation.ModerationPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class WelcomeListener implements Listener {

    private final String prefix;

    public WelcomeListener() {
        prefix = ChatColor.translateAlternateColorCodes('&',
            ModerationPlugin.getInstance().getConfig().getString("prefix", "&8[&cModeration&8] &r")
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        String playerName = ChatColor.BLUE + event.getPlayer().getName() + ChatColor.RESET;
        int onlineCount = Bukkit.getOnlinePlayers().size();

        String message = prefix + ChatColor.RED + "Welcome " + playerName +
                ChatColor.RED + " on our Server. Right now there are " +
                ChatColor.GREEN + onlineCount + ChatColor.RED + " members online.";

        Bukkit.broadcastMessage(message);
    }
}