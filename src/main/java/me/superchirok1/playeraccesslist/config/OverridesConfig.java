package me.superchirok1.playeraccesslist.config;

import me.superchirok1.playeraccesslist.PlayerAccessList;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class OverridesConfig {

    private final PlayerAccessList plugin;

    private ConfigurationSection overrides;
    private YamlConfiguration config;

    public OverridesConfig(PlayerAccessList plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        this.config = plugin.getOverrides();
        this.overrides = config.getConfigurationSection("overrides");
    }

    public ConfigurationSection getOverrides() {
        return overrides;
    }

    public YamlConfiguration getConfig() {
        return config;
    }
}
