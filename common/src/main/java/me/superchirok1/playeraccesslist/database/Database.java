package me.superchirok1.playeraccesslist.database;

import me.superchirok1.playeraccesslist.PlayerAccessList;
import me.superchirok1.playeraccesslist.list.ListType;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public abstract class Database {

    private final PlayerAccessList plugin;

    public Database(PlayerAccessList plugin) {
        this.plugin = plugin;
    }

    protected Connection connection;

    protected String sqlAdd;
    protected String sqlRemove;
    protected String sqlHas;
    protected String sqlList;

    protected String getQuery(String template, ListType type) {
        return template.replace("%table%", type.name().toLowerCase());
    }

    public abstract void initTables() throws SQLException;
    public void close() throws SQLException {
        connection.close();
    }

    public void add(String value, ListType listType) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                try (PreparedStatement statement = connection.prepareStatement(
                        getQuery(sqlAdd, listType)
                )) {

                    statement.setString(1, value);
                    statement.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                plugin.getLogger().severe("Async DB Error: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void remove(String value, ListType listType) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                try (PreparedStatement statement = connection.prepareStatement(
                        getQuery(sqlRemove, listType)
                )) {

                    statement.setString(1, value);
                    statement.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                plugin.getLogger().severe("Async DB Error: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public Set<String> list(ListType listType) {
        Set<String> players = new HashSet<>();

        try (PreparedStatement statement = connection.prepareStatement(getQuery(sqlList, listType))) {
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                players.add(rs.getString("value"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return players;
    }

}
