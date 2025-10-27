package me.superchirok1.playeraccesslist.config;

import me.superchirok1.playeraccesslist.PlayerAccessList;
import org.bukkit.configuration.file.YamlConfiguration;

public class MessagesConfig {

    private final PlayerAccessList plugin;
    private YamlConfiguration cfg;

    private String phWhitelist;
    private String phBlacklist;
    private String phTrue;
    private String phFalse;

    private String prefix;
    private String reloaded;
    private String noPerms;
    private String added;
    private String usage;
    private String switchedTo;
    private String noSuchThing;
    private String removed;
    private String suchAlreadyExists;
    private String kickLog;
    private String kickLogWl;
    private String kickLogBl;
    private String status;

    public MessagesConfig(PlayerAccessList plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        this.cfg = plugin.getMessages();

        this.phWhitelist = cfg.getString("placeholders.whitelist", "Whitelist");
        this.phBlacklist = cfg.getString("placeholders.blacklist", "Blacklist");
        this.phTrue = cfg.getString("placeholders.true", "true");
        this.phFalse = cfg.getString("placeholders.false", "false");

        this.prefix = cfg.getString("messages.prefix", "&7[Access]");
        this.reloaded = cfg.getString("messages.reloaded", "&aReloaded!");
        this.noPerms = cfg.getString("messages.no-perms", "&cНет прав!");
        this.added = cfg.getString("messages.added", "&aДобавлен {plr} в {list}");
        this.usage = cfg.getString("messages.usage", "&7Использование: {command}");
        this.switchedTo = cfg.getString("messages.switched-to", "&7{list} теперь {boolean}");
        this.noSuchThing = cfg.getString("messages.no-such-thing", "&cТакого нет");
        this.removed = cfg.getString("messages.removed", "&c{plr} удалён из {list}");
        this.suchAlreadyExists = cfg.getString("messages.such-already-exists", "&cУже есть!");
        this.kickLog = cfg.getString("messages.kick-log", "&cИгрок {plr} был кикнут");
        this.kickLogWl = cfg.getString("messages.kick-log-wl", "&eИгрок {plr} не в белом списке");
        this.kickLogBl = cfg.getString("messages.kick-log-bl", "&cИгрок {plr} в черном списке");
        this.status = cfg.getString("messages.status", "Статус: {boolean}");
    }

    public String getPhWhitelist() { return phWhitelist; }
    public String getPhBlacklist() { return phBlacklist; }
    public String getPhTrue() { return phTrue; }
    public String getPhFalse() { return phFalse; }

    public String getPrefix() { return prefix; }
    public String getReloaded() { return reloaded; }
    public String getNoPerms() { return noPerms; }
    public String getAdded() { return added; }
    public String getUsage() { return usage; }
    public String getSwitchedTo() { return switchedTo; }
    public String getNoSuchThing() { return noSuchThing; }
    public String getRemoved() { return removed; }
    public String getSuchAlreadyExists() { return suchAlreadyExists; }
    public String getKickLog() { return kickLog; }
    public String getKickLogWl() { return kickLogWl; }
    public String getKickLogBl() { return kickLogBl; }
    public String getStatus() { return status; }
}
