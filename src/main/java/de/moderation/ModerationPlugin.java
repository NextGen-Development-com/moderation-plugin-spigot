package de.moderation;

import de.moderation.commands.*;
import de.moderation.listener.*;
import org.bukkit.plugin.java.JavaPlugin;

public class ModerationPlugin extends JavaPlugin {
    private static ModerationPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getCommand("kick").setExecutor(new KickCommand(this));
        getCommand("mute").setExecutor(new MuteCommand(this));
        getCommand("warn").setExecutor(new WarnCommand(this));
        getCommand("unmute").setExecutor(new UnmuteCommand(this));
        getCommand("fly").setExecutor(new FlyCommand(this));
        getCommand("gm").setExecutor(new GamemodeCommand(this));
        getCommand("help").setExecutor(new HelpCommand(this));
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getLogger().info("ModerationPlugin by NextGen Development enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("ModerationPlugin by NextGen Development disabled.");
    }

    public static ModerationPlugin getInstance() {
        return instance;
    }
}