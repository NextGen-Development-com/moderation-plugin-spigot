package de.moderation.database;

import de.moderation.ModerationPlugin;

import java.io.File;
import java.sql.*;
import java.util.UUID;

public class Database {

    private Connection connection;

    public Database(ModerationPlugin plugin) {
        try {
            File dataFolder = plugin.getDataFolder();
            if (!dataFolder.exists()) dataFolder.mkdirs();

            String url = "jdbc:sqlite:" + new File(dataFolder, "database.db").getAbsolutePath();
            connection = DriverManager.getConnection(url);

            createTables();
        } catch (SQLException ignored) {}
    }

    private void createTables() {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS muted_players (" +
                "uuid TEXT PRIMARY KEY," +
                "reason TEXT" +
                ")"
            );
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS player_money (" +
                "uuid TEXT PRIMARY KEY," +
                "balance INTEGER DEFAULT 0" +
                ")"
            );
        } catch (SQLException ignored) {}
    }

    public Connection getConnection() {
    return connection;
    }

    public void mutePlayer(UUID uuid, String reason) {
        try (PreparedStatement ps = connection.prepareStatement(
                "REPLACE INTO muted_players (uuid, reason) VALUES (?, ?)"
        )) {
            ps.setString(1, uuid.toString());
            ps.setString(2, reason);
            ps.executeUpdate();
        } catch (SQLException ignored) {}
    }

    public void unmutePlayer(UUID uuid) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM muted_players WHERE uuid = ?"
        )) {
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException ignored) {}
    }

    public boolean isMuted(UUID uuid) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT 1 FROM muted_players WHERE uuid = ?"
        )) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ignored) {
            return false;
        }
    }

    public long getBalance(UUID uuid) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT balance FROM player_money WHERE uuid = ?"
        )) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong("balance");
                else {
                    setBalance(uuid, 0);
                    return 0;
                }
            }
        } catch (SQLException ignored) {
            return 0;
        }
    }

    public void setBalance(UUID uuid, long amount) {
        try (PreparedStatement ps = connection.prepareStatement(
                "REPLACE INTO player_money (uuid, balance) VALUES (?, ?)"
        )) {
            ps.setString(1, uuid.toString());
            ps.setLong(2, amount);
            ps.executeUpdate();
        } catch (SQLException ignored) {}
    }

    public boolean withdraw(UUID uuid, long amount) {
        long current = getBalance(uuid);
        if (current < amount) return false;
        setBalance(uuid, current - amount);
        return true;
    }

    public void deposit(UUID uuid, long amount) {
        long current = getBalance(uuid);
        setBalance(uuid, current + amount);
    }
}