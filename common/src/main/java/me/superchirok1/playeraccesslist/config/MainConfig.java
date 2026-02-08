package me.superchirok1.playeraccesslist.config;

import me.superchirok1.playeraccesslist.PlayerAccessList;
import me.superchirok1.playeraccesslist.color.Serializer;
import me.superchirok1.playeraccesslist.database.DatabaseType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MainConfig {

    public GeneralValues general;
    public DatabaseValues database;

    public void initConfig(PlayerAccessList plugin) {
        File configFile = new File(plugin.getDataFolder(), "settings.yml");
        if (!configFile.exists()) {
            plugin.saveResource("settings.yml", false);
        }
        FileConfiguration conf = YamlConfiguration.loadConfiguration(configFile);

        Serializer serializer;
        try {
            serializer = Serializer.valueOf(conf.getString("serializer", "LEGACY").toUpperCase());
        } catch (IllegalArgumentException e) {
            serializer = Serializer.LEGACY;
        }

        general = new GeneralValues(
                conf.getString("prefix", ""),
                serializer,
                conf.getString("language", "en")
        );

        DatabaseType dbType;
        try {
            dbType = DatabaseType.valueOf(conf.getString("database.type", "SQLITE").toUpperCase());
        } catch (IllegalArgumentException e) {
            dbType = DatabaseType.SQLITE;
        }

        database = new DatabaseValues(
                dbType,
                conf.getString("database.sqlite_path", "data.db"),
                conf.getString("database.mysql.host", "0.0.0.0"),
                conf.getInt("database.mysql.port", 3306),
                conf.getString("database.mysql.username", "pal_user"),
                conf.getString("database.mysql.password", "password"),
                conf.getString("database.mysql.database", "pal_db"),
                conf.getString("database.mysql.args", "?useSSL=false&autoReconnect=true&characterEncoding=utf8"),
                conf.getBoolean("database.mysql.polling.enabled", false),
                conf.getInt("database.mysql.polling.delay", 60)
        );
    }

    public record GeneralValues(
            String prefix,
            Serializer serializer,
            String language
    ) {}

    public record DatabaseValues(
            DatabaseType type,
            String sqliteFile,
            String host,
            int port,
            String username,
            String pass,
            String dbname,
            String args,
            boolean polling,
            int pollingDelay
    ) {}
}
