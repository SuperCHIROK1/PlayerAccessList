package me.superchirok1.playeraccesslist.util;

import me.superchirok1.playeraccesslist.PlayerAccessList;
import me.superchirok1.playeraccesslist.color.Colorizer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LocalizationProvider {

    private final PlayerAccessList pl;
    private String prefix = "";

    public LocalizationProvider(PlayerAccessList pl) {
        this.pl = pl;
    }

    public Values get;

    public void init() {
        var config = pl.config.general;
        String fileName = "lang/lang-" + config.language().toLowerCase() + ".yml";

        File langFile = new File(pl.getDataFolder(), fileName);
        if (!langFile.exists()) {
            pl.getLogger().severe("Language file " + fileName + " does not exist.");
            return;
        }
        FileConfiguration conf = YamlConfiguration.loadConfiguration(langFile);

        prefix = config.prefix();

        get = new Values(
                format(conf.getString("no_perms", "")),
                format(conf.getString("reloaded", "")),
                format(conf.getString("usage", "")),
                format(conf.getString("admin_usage", "")),
                format(conf.getString("migrate.usage", "")),
                format(conf.getString("migrate.select_list", "")),
                format(conf.getString("migrate.successful", "")),
                format(conf.getString("whitelist.added", "")),
                format(conf.getString("whitelist.removed", "")),
                format(conf.getString("whitelist.such_already_exists", "")),
                format(conf.getString("whitelist.status", "")),
                format(conf.getString("whitelist.no_such_thing", "")),
                format(conf.getString("whitelist.cannot_be_added", "")),
                format(conf.getString("blacklist.added", "")),
                format(conf.getString("blacklist.removed", "")),
                format(conf.getString("blacklist.such_already_exists", "")),
                format(conf.getString("blacklist.status", "")),
                format(conf.getString("blacklist.no_such_thing", "")),
                format(conf.getString("blacklist.cannot_be_added", "")),
                format(conf.getString("placeholders.true", "")),
                format(conf.getString("placeholders.false", ""))
        );
    }

    private String format(String text) {
        if (text == null) return "";
        return Colorizer.get.colorize(text.replace("%prefix%", prefix));
    }

    public record Values(
            String noPerms,
            String reloaded,
            String usage,
            String admin_usage,
            String mUsage,
            String mSelectList,
            String mSuccessful,
            String wAdded,
            String wRemoved,
            String wExists,
            String wStatus,
            String wNoSuchThing,
            String wCannotBeAdded,
            String bAdded,
            String bRemoved,
            String bExists,
            String bStatus,
            String bNoSuchThing,
            String bCannotBeAdded,
            String pTrue,
            String pFalse
    ) {}
}
