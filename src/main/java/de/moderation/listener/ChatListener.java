package de.moderation.listener;

import de.moderation.ModerationPlugin;
import de.moderation.commands.MuteCommand;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private final String blockedMsg;

    public ChatListener() {
        String prefix = ChatColor.translateAlternateColorCodes('&',
            ModerationPlugin.getInstance().getConfig().getString("prefix", "&8[&cModeration&8] &r")
        );
        this.blockedMsg = ChatColor.translateAlternateColorCodes('&',
            ModerationPlugin.getInstance()
                .getConfig()
                .getString("messages.chat-blocked", "{prefix}&cYou are muted and cannot chat.")
                .replace("{prefix}", prefix)
        );
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (MuteCommand.mutedPlayers.contains(event.getPlayer().getName())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(blockedMsg);
        }
    }
}