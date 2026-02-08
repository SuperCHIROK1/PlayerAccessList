package me.superchirok1.playeraccesslist.config;

import me.superchirok1.playeraccesslist.PlayerAccessList;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class AccessConfig {

    public Whitelist whitelist;
    public Blacklist blacklist;
    public Overrides overrides;
    public NicknameCheck nickname;

    public void initConfig(PlayerAccessList plugin) {

        File configFile = new File(plugin.getDataFolder(), "access.yml");
        if (!configFile.exists()) {
            plugin.saveResource("access.yml", false);
        }
        FileConfiguration conf = YamlConfiguration.loadConfiguration(configFile);

        whitelist = new Whitelist(
                conf.getBoolean("whitelist.enabled", false),
                conf.getStringList("whitelist.kick_message"),
                conf.getStringList("whitelist.cannot_be_added")
        );

        blacklist = new Blacklist(
                conf.getBoolean("blacklist.enabled", false),
                conf.getStringList("blacklist.kick_message"),
                conf.getStringList("blacklist.cannot_be_added")
        );

        overrides = new Overrides(
                conf.getBoolean("overrides.enabled", false),
                conf.getStringList("overrides.files")
        );


        nickname = new NicknameCheck(
                conf.getBoolean("nickname_check.enabled", false),
                conf.getStringList("nickname_check.kick_message"),
                Pattern.compile(conf.getString("nickname_check.regex", ".*"))
        );
    }

    public void toggleWhitelist(PlayerAccessList plugin, boolean state) {
        toggle("whitelist", plugin, state);
    }

    public void toggleBlacklist(PlayerAccessList plugin, boolean state) {
        toggle("blacklist", plugin, state);
    }

    public void toggle(String section, PlayerAccessList plugin, boolean state) {
        File configFile = new File(plugin.getDataFolder(), "access.yml");
        FileConfiguration conf = YamlConfiguration.loadConfiguration(configFile);

        conf.set(section + ".enabled", state);

        try {
            conf.save(configFile);
            this.initConfig(plugin);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config to " + configFile.getName());
            e.printStackTrace();
        }
    }

    public record Whitelist(boolean enable, List<String> kick, List<String> cannotBeAdded) {}
    public record Blacklist(boolean enable, List<String> kick, List<String> cannotBeAdded) {}
    public record Overrides(boolean enable, List<String> files) {}
    public record NicknameCheck(boolean enable, List<String> kickMessage, Pattern regex) {}

}
