package me.superchirok1.playeraccesslist.config;

import me.superchirok1.playeraccesslist.PlayerAccessList;
import me.superchirok1.playeraccesslist.color.Colorizer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LogConfig {

    public Values get;

    public void init(PlayerAccessList plugin) {
        File configFile = new File(plugin.getDataFolder(), "logging.yml");
        if (!configFile.exists()) {
            plugin.saveResource("logging.yml", false);
        }
        FileConfiguration conf = YamlConfiguration.loadConfiguration(configFile);

        MessageType type;
        try {
            type = MessageType.valueOf(conf.getString("message_type", "CHAT_MESSAGE").toUpperCase());
        } catch (IllegalArgumentException e) {
            type = MessageType.CHAT_MESSAGE;
        }

        get = new Values(
                conf.getBoolean("moderator_log", false),
                conf.getBoolean("console_log", false),
                type,
                conf.getString("sound", ""),
                Colorizer.get.colorize(conf.getString("messages.whitelist",
                        "Игрок &f%player% (%ip%) &7пытался войти, но он не был в вайтлисте.")),
                Colorizer.get.colorize(conf.getString("messages.blacklist",
                        "Игрок &f%player% (%ip%) &7пытался войти, но он был в ч.с.")),
                Colorizer.get.colorize(conf.getString("messages.kick",
                        "Игрок &f%player% (%ip%) &7пытался войти."))
        );
    }

    public record Values(
            boolean moderator,
            boolean console,
            MessageType messageType,
            String sound,
            String whitelist,
            String blacklist,
            String kick
    ){}

    public enum MessageType {
        CHAT_MESSAGE, ACTIONBAR
    }
}
