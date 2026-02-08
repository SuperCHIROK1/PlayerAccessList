package me.superchirok1.playeraccesslist.database.impl;

import me.superchirok1.playeraccesslist.PlayerAccessList;
import me.superchirok1.playeraccesslist.database.Database;
import org.bukkit.Bukkit;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDatabase extends Database {
    private final PlayerAccessList plugin;

    private final String path;

    public SQLiteDatabase(PlayerAccessList plugin, String path) {
        super(plugin);
        this.plugin = plugin;
        this.path = plugin.getDataFolder().getAbsolutePath() + "/" + path;

        this.sqlAdd = "INSERT OR IGNORE INTO %table% (value) VALUES (?)";
        this.sqlRemove = "DELETE FROM %table% WHERE value = ?";
        this.sqlHas = "SELECT 1 FROM %table% WHERE value = ? LIMIT 1";
        this.sqlList = "SELECT value FROM %table%";
    }

    @Override
    public void initTables() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:"+path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS whitelist (value TEXT PRIMARY KEY);");
            statement.execute("CREATE TABLE IF NOT EXISTS blacklist (value TEXT PRIMARY KEY);");
        } catch (SQLException e) {
            plugin.getLogger().severe("The plugin will be disabled. Reason:");
            plugin.getLogger().severe("> Failed to create tables: "+e.getMessage());
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }
}
