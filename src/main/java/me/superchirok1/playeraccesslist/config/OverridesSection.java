package me.superchirok1.playeraccesslist.config;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class OverridesSection {

    private final boolean enabled;

    private final String permission;
    private final String world;
    private final String name;
    private final String uuid;

    private final List<String> message;

    public boolean isEnabled() {
        return enabled;
    }

    public String getPermission() {
        return permission;
    }

    public String getWorld() {
        return world;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return name;
    }

    public List<String> getMessage() {
        return message;
    }

    public OverridesSection(ConfigurationSection section) {
        this.enabled = section.getBoolean("enabled", false);

        ConfigurationSection conditions = section.getConfigurationSection("conditions");
        this.permission = conditions.getString("permission", "");
        this.world = conditions.getString("world", "");
        this.name = conditions.getString("name", "");
        this.uuid = conditions.getString("uuid", "");

        this.message = section.getStringList("message");
    }
}
