package me.superchirok1.playeraccesslist;

import me.superchirok1.playeraccesslist.commands.BlacklistCommand;
import me.superchirok1.playeraccesslist.commands.ReloadCommand;
import me.superchirok1.playeraccesslist.commands.WhitelistCommand;
import me.superchirok1.playeraccesslist.config.MainConfig;
import me.superchirok1.playeraccesslist.config.MessagesConfig;
import me.superchirok1.playeraccesslist.config.OverridesConfig;
import me.superchirok1.playeraccesslist.util.Text;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class PlayerAccessList extends JavaPlugin {

    private BukkitAudiences adventure;
    private AccessLists accessLists;

    private MainConfig mainConfig;
    private MessagesConfig messagConfig;
    private OverridesConfig overrideConfig;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        saveResourceIfNotExists("messages.yml");
        saveResourceIfNotExists("overrides.yml");

        create();

        mainConfig = new MainConfig(this);
        messagConfig = new MessagesConfig(this);
        overrideConfig = new OverridesConfig(this);

        adventure = BukkitAudiences.create(this);
        Text.init(adventure);
        Text.setPlugin(this);

        this.accessLists = new AccessLists(this);

        getCommand("whitelist").setExecutor(new WhitelistCommand(this));
        getCommand("whitelist").setTabCompleter(new WhitelistCommand(this));
        getCommand("blacklist").setExecutor(new BlacklistCommand(this));
        getCommand("blacklist").setTabCompleter(new BlacklistCommand(this));
        getCommand("pal").setExecutor(new ReloadCommand(this));

        getServer().getPluginManager().registerEvents(new Listener(this), this);
    }

    public AccessLists getAccessLists() {
        return accessLists;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void saveResourceIfNotExists(String resourcePath) {
        File file = new File(getDataFolder(), resourcePath);
        if (!file.exists()) {
            saveResource(resourcePath, false);
        }
    }

    public YamlConfiguration load(String resource) {
        File file = new File(getDataFolder(), resource);
        if (!file.exists()) {
            saveResource(resource, false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        return config;
    }

    private YamlConfiguration messagesConfig;
    private YamlConfiguration overridesConfig;

    public void create() {
        reloadConfig();
        this.messagesConfig = load(new MainConfig(this).getMessagesFile());
        this.overridesConfig = load(new MainConfig(this).getOverridesFile());
    }

    public void reload() {
        reloadConfig();
        messagConfig.reload();
        overrideConfig.reload();
    }

    public YamlConfiguration getMessages() {
        return messagesConfig;
    }

    public YamlConfiguration getOverrides() {
        return overridesConfig;
    }

    public MainConfig getConf() {
        return mainConfig;
    }

    public MessagesConfig getMessagesConf() {
        return messagConfig;
    }

    public OverridesConfig getOverridesConf() {
        return overrideConfig;
    }

}
