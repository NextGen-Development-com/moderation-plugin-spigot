package de.moderation;

import de.moderation.commands.Moderation.*;
import de.moderation.commands.Utility.*;
import de.moderation.commands.Economy.*;
import de.moderation.database.Database;
import de.moderation.listener.*;
import org.bukkit.plugin.java.JavaPlugin;

public class ModerationPlugin extends JavaPlugin {
    private static ModerationPlugin instance;
    private Database database;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        database = new Database(this);

        getCommand("kick").setExecutor(new KickCommand(this));
        getCommand("mute").setExecutor(new MuteCommand(this));
        getCommand("unmute").setExecutor(new UnmuteCommand(this));
        getCommand("fly").setExecutor(new FlyCommand(this));
        getCommand("gm").setExecutor(new GamemodeCommand(this));
        getCommand("help").setExecutor(new HelpCommand(this));
        getCommand("balance").setExecutor(new BalanceCommand(this));
        getCommand("pay").setExecutor(new PayCommand(this));
        getCommand("leaderboard").setExecutor(new LeaderboardCommand(this));
        getCommand("eco").setExecutor(new EcoCommand(this));

        getCommand("announce").setExecutor(new AnnouncementCommand());
        getCommand("vanish").setExecutor(new VanishCommand(this));
        getCommand("feed").setExecutor(new FeedCommand());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("clearchat").setExecutor(new ClearChatCommand());
        getCommand("craft").setExecutor(new CraftCommand());
        getCommand("enchant").setExecutor(new EnchantCommand());
        getCommand("ec").setExecutor(new EnderChestCommand());

        GodCommand godCommand = new GodCommand(this);
        getCommand("god").setExecutor(godCommand);
        getServer().getPluginManager().registerEvents(godCommand, this);


        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new WelcomeListener(), this);
        getServer().getPluginManager().registerEvents(new LeaveListener(), this);
        getServer().getPluginManager().registerEvents(new TreeMinerListener(), this);

        getLogger().info("ModerationPlugin by NextGen Development enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("ModerationPlugin by NextGen Development disabled.");
    }

    public static ModerationPlugin getInstance() {
        return instance;
    }

    public Database getDatabase() {
        return database;
    }
}