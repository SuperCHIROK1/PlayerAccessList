package me.superchirok1.playeraccesslist.database.impl;

import me.superchirok1.playeraccesslist.PlayerAccessList;
import me.superchirok1.playeraccesslist.database.Database;
import org.bukkit.Bukkit;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlDatabase extends Database {
    private final PlayerAccessList plugin;

    private final String url;
    private final String user;
    private final String password;

    public MySqlDatabase(PlayerAccessList plugin, String host, int port, String db, String user, String password, String args) {
        super(plugin);
        this.plugin = plugin;

        this.user = user;
        this.password = password;

        this.url = "jdbc:mysql://" + host + ":" + port + "/" + db + args;

        this.sqlAdd = "INSERT IGNORE INTO %table% (value) VALUES (?)";
        this.sqlRemove = "DELETE FROM %table% WHERE value = ?";
        this.sqlHas = "SELECT 1 FROM %table% WHERE value = ? LIMIT 1";
        this.sqlList = "SELECT value FROM %table%";
    }

    @Override
    public void initTables() throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
        //Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS whitelist (value VARCHAR(255) PRIMARY KEY);");
            statement.execute("CREATE TABLE IF NOT EXISTS blacklist (value VARCHAR(255) PRIMARY KEY);");
        } catch (SQLException e) {
            plugin.getLogger().severe("The plugin will be disabled. Reason:");
            plugin.getLogger().severe("> Failed to create tables: "+e.getMessage());
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
        //});
    }
}
