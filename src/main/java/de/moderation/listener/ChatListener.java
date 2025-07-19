package de.moderation.listener;

import de.moderation.ModerationPlugin;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class ChatListener implements Listener {
    private final String blockedMsg;
    private final List<String> blockedCommands;

    public ChatListener() {
        FileConfiguration config = ModerationPlugin.getInstance().getConfig();
        String prefix = ChatColor.translateAlternateColorCodes('&',
            config.getString("prefix", "&8[&cModeration&8] &r")
        );
        this.blockedMsg = ChatColor.translateAlternateColorCodes('&',
            config.getString("messages.chat-blocked", "{prefix}&cYou are muted and cannot chat.")
                .replace("{prefix}", prefix)
        );
        this.blockedCommands = config.getStringList("blocked-chat-commands");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (ModerationPlugin.getInstance().getDatabase().isMuted(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(blockedMsg);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (!ModerationPlugin.getInstance().getDatabase().isMuted(event.getPlayer().getUniqueId())) return;

        String msg = event.getMessage().toLowerCase();
        for (String blocked : blockedCommands) {
            if (msg.startsWith("/" + blocked + " ") || msg.equals("/" + blocked)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(blockedMsg);
                break;
            }
        }
    }
}