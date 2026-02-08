package me.superchirok1.playeraccesslist;

import me.superchirok1.playeraccesslist.cache.CacheProvider;
import me.superchirok1.playeraccesslist.color.Colorizer;
import me.superchirok1.playeraccesslist.color.impl.LegacySerializer;
import me.superchirok1.playeraccesslist.command.BlacklistCommand;
import me.superchirok1.playeraccesslist.command.PALCommand;
import me.superchirok1.playeraccesslist.command.WhitelistCommand;
import me.superchirok1.playeraccesslist.condition.ConditionManager;
import me.superchirok1.playeraccesslist.config.AccessConfig;
import me.superchirok1.playeraccesslist.config.LogConfig;
import me.superchirok1.playeraccesslist.config.MainConfig;
import me.superchirok1.playeraccesslist.database.Database;
import me.superchirok1.playeraccesslist.database.DatabaseType;
import me.superchirok1.playeraccesslist.database.impl.MySqlDatabase;
import me.superchirok1.playeraccesslist.database.impl.SQLiteDatabase;
import me.superchirok1.playeraccesslist.events.BlacklistCancelJoinEvent;
import me.superchirok1.playeraccesslist.events.WhitelistCancelJoinEvent;
import me.superchirok1.playeraccesslist.list.AccessList;
import me.superchirok1.playeraccesslist.list.ListType;
import me.superchirok1.playeraccesslist.list.impl.Blacklist;
import me.superchirok1.playeraccesslist.list.impl.Whitelist;
import me.superchirok1.playeraccesslist.listener.MainListener;
import me.superchirok1.playeraccesslist.util.*;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public final class PlayerAccessList extends JavaPlugin {

    public Whitelist whitelist;
    public Blacklist blacklist;
    public CacheProvider cacheProvider;

    public Database database;

    public MainConfig config;
    public AccessConfig accessConfig;
    public LogConfig logConfig;

    public LogHandler logHandler;

    public Colorizer colorizer;
    private LegacySerializer legacySerializer;

    public LocalizationProvider locale;

    public ConditionManager conditionManager;
    public Overrides overrides;

    public MigrateManager migrateManager;

    private final List<String> files = Arrays.asList(
            "settings.yml", "access.yml", "logging.yml", "lang/lang-ru.yml", "overrides/overrides.yml",
            "lang/lang-en.yml"
    );

    @Override
    public void onEnable() {

        long start = System.currentTimeMillis();
        Metrics metrics = new Metrics(this, 29094);

        legacySerializer = new LegacySerializer();
        sendWelcome("#00ff8c");

        firstStartSave();
        loadConfigNodes();

        metrics.addCustomChart(new Metrics.SimplePie("used_language", () -> {
            return config.general.language();
        }));

        metrics.addCustomChart(new Metrics.SimplePie("used_colorizers", () -> {
            return config.general.serializer().name();
        }));

        boolean mysql = config.database.type() == DatabaseType.MYSQL;

        database = mysql
                ? new MySqlDatabase(
                        this,
                        config.database.host(), config.database.port(),
                        config.database.dbname(), config.database.username(),
                        config.database.pass(), config.database.args()
                )
                : new SQLiteDatabase(this, config.database.sqliteFile());
        try {
            database.initTables();
        } catch (SQLException e) {
            getLogger().severe("Could not connect to database! Reason: "+e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        cacheProvider = new CacheProvider();
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            setCacheFromDatabase();
        });

        whitelist = new Whitelist(this);
        blacklist = new Blacklist(this);

        logHandler = new LogHandler(this);

        locale = new LocalizationProvider(this);
        locale.init();

        conditionManager = new ConditionManager();
        conditionManager.init();

        overrides = new Overrides(this);
        overrides.init();

        migrateManager = new MigrateManager(this);

        getServer().getPluginManager().registerEvents(new MainListener(this), this);

        PluginCommand wlCommand = getCommand("whitelist");
        WhitelistCommand whitelistCommand = new WhitelistCommand(this);
        wlCommand.setExecutor(whitelistCommand);
        wlCommand.setTabCompleter(whitelistCommand);

        PluginCommand blCommand = getCommand("blacklist");
        BlacklistCommand blacklistCommand = new BlacklistCommand(this);
        blCommand.setExecutor(blacklistCommand);
        blCommand.setTabCompleter(blacklistCommand);

        PluginCommand palCommand = getCommand("pal");
        PALCommand palClassCommand = new PALCommand(this);
        palCommand.setExecutor(palClassCommand);
        palCommand.setTabCompleter(palClassCommand);

        long duration = System.currentTimeMillis() - start;
        sendConsole("  &#00ff8cThe plugin was enabled in &f"+duration+"&#00ff8c ms.");
        sendConsole("  ");
        sendConsole("  ");

        new PlaceholderHook(this).register();

        if (mysql && config.database.polling()) {
            long delaySeconds = 20L * config.database.pollingDelay();
            Bukkit.getScheduler().runTaskTimerAsynchronously(this, this::setCacheFromDatabase,
                    delaySeconds, delaySeconds);
        }

    }

    @Override
    public void onDisable() {
        if (database != null) {
            try {
                database.close();
            } catch (SQLException e) {
                getLogger().severe("Failed to close database: " + e.getMessage());
            }
        }
    }

    public AccessList getList(ListType type) {
        return type == ListType.WHITELIST ? whitelist : blacklist;
    }

    private void setCacheFromDatabase() {
        try {
            Set<String> wl = database.list(ListType.WHITELIST);
            Set<String> bl = database.list(ListType.BLACKLIST);

            cacheProvider.setWhitelist(wl);
            cacheProvider.setBlacklist(bl);
        } catch (Exception e) {
            getLogger().warning("Failed to sync cache with database: " + e.getMessage());
        }
    }

    private void loadConfigNodes() {

        config = new MainConfig();
        config.initConfig(this);

        colorizer = new Colorizer();
        colorizer.init(config.general.serializer());

        accessConfig = new AccessConfig();
        accessConfig.initConfig(this);

        logConfig = new LogConfig();
        logConfig.init(this);

    }

    public void reload() {

        loadConfigNodes();

        cacheProvider.setWhitelist(database.list(ListType.WHITELIST));
        cacheProvider.setBlacklist(database.list(ListType.BLACKLIST));

        locale = new LocalizationProvider(this);
        locale.init();

        overrides.init();

    }

    public boolean isFirstLaunch() {
        return !getDataFolder().exists();
    }

    private void firstStartSave() {
        if (!isFirstLaunch()) return;

        for (String file : files) {
            File configFile = new File(getDataFolder(), file);

            if (!configFile.exists()) {
                saveResource(file, false);
            }
        }
    }

    public void sendConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(legacySerializer.colorize(message));
    }

    private void sendWelcome(String bukva) {
        sendConsole("                                                    ");
        sendConsole("                                                    ");
        sendConsole("  &"+bukva+"█████▄ ██     ▄████▄ ▄█████ ▄█████ ██     ██ ▄█████ ██████ ");
        sendConsole("  &"+bukva+"██▄▄█▀ ██     ██▄▄██ ██     ██     ██     ██ ▀▀▀▄▄▄   ██   ");
        sendConsole("  &"+bukva+"██     ██████ ██  ██ ▀█████ ▀█████ ██████ ██ █████▀   ██   ");
        sendConsole("                                                    ");
        sendConsole("  &fVersion: &"+ bukva + getDescription().getVersion()+ " &8| &fAuthor: &"+bukva+"SuperCHIROK1");
        sendConsole("  &fGitHub: &"+bukva+"https://github.com/SuperCHIROK1/PlayerAccessList");
        sendConsole("  &fContributors: &"+bukva+"https://github.com/SuperCHIROK1/PlayerAccessList/blob/main/CONTRIBUTORS.md");
        sendConsole("                                                    ");
        if (isFirstLaunch()) {
            sendConsole("    &aDetected the first launch of PlayerAccessList!");
            sendConsole("    &fThank you for using PlayerAccessList!");
            sendConsole("    ");
        }
    }


}
