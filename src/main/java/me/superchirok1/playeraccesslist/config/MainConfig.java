package me.superchirok1.playeraccesslist.config;

import me.superchirok1.playeraccesslist.PlayerAccessList;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.List;

public class MainConfig {

    private final PlayerAccessList plugin;
    private FileConfiguration config;

    private boolean logConsole;
    private boolean logAdmin;

    private String dataStorage;
    private String overridesFile;
    private String messagesFile;

    private boolean whitelistEnabled;
    private boolean blacklistEnabled;

    private List<String> whitelistMessage;
    private List<String> blacklistMessage;

    private List<String> whitelistCommandsOnEntry;
    private List<String> whitelistCommandsOnKick;

    private List<String> blacklistCommandsOnEntry;
    private List<String> blacklistCommandsOnKick;

    private boolean bypassPermissions;

    public MainConfig(PlayerAccessList plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();

        this.logConsole = config.getBoolean("log.console", false);
        this.logAdmin = config.getBoolean("log.admin", false);
        this.dataStorage = config.getString("data.storage", "YAML");
        this.overridesFile = config.getString("data.overrides", "overrides.yml");
        this.messagesFile = config.getString("data.messages", "messages.yml");
        this.whitelistEnabled = config.getBoolean("access.whitelist.enabled", false);
        this.blacklistEnabled = config.getBoolean("access.blacklist.enabled", false);
        this.whitelistMessage = config.getStringList("access.whitelist.message");
        this.blacklistMessage = config.getStringList("access.blacklist.message");
        this.whitelistCommandsOnEntry = config.getStringList("access.whitelist.commands.on-entry");
        this.whitelistCommandsOnKick = config.getStringList("access.whitelist.commands.on-kick");
        this.blacklistCommandsOnEntry = config.getStringList("access.blacklist.commands.on-entry");
        this.blacklistCommandsOnKick = config.getStringList("access.blacklist.commands.on-kick");
        this.bypassPermissions = config.getBoolean("access.bypass-permission", false);
    }

    public boolean toggleWl() {
        boolean newValue = !whitelistEnabled;
        plugin.getConfig().set("access.whitelist.enabled", newValue);
        plugin.saveConfig();
        this.whitelistEnabled = newValue;
        return newValue;
    }

    public boolean toggleBl() {
        boolean newValue = !blacklistEnabled;
        plugin.getConfig().set("access.blacklist.enabled", newValue);
        plugin.saveConfig();
        this.blacklistEnabled = newValue;
        return newValue;
    }

    public boolean isLogConsole() { return logConsole; }
    public boolean isLogAdmin() { return logAdmin; }
    public String getDataStorage() { return dataStorage; }
    public String getOverridesFile() { return overridesFile; }
    public String getMessagesFile() { return messagesFile; }
    public boolean isWhitelistEnabled() { return whitelistEnabled; }
    public boolean isBlacklistEnabled() { return blacklistEnabled; }
    public List<String> getWhitelistMessage() { return whitelistMessage; }
    public List<String> getBlacklistMessage() { return blacklistMessage; }
    public List<String> getWhitelistCommandsOnEntry() { return whitelistCommandsOnEntry; }
    public List<String> getWhitelistCommandsOnKick() { return whitelistCommandsOnKick; }
    public List<String> getBlacklistCommandsOnEntry() { return blacklistCommandsOnEntry; }
    public List<String> getBlacklistCommandsOnKick() { return blacklistCommandsOnKick; }
    public boolean isBypassPermissions() { return bypassPermissions; }
}
