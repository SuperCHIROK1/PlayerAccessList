package me.superchirok1.playeraccesslist;

import me.superchirok1.playeraccesslist.color.Colorizer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Overrides {

    public final Set<Override> overrides = new HashSet<>();

    private final PlayerAccessList plugin;

    public Overrides(PlayerAccessList plugin) {
        this.plugin = plugin;
    }

    public void init() {

        overrides.clear();

        if (!plugin.accessConfig.overrides.enable()) return;
        List<String> files = plugin.accessConfig.overrides.files();

        for (String fileName : files) {

            File file = new File(plugin.getDataFolder(), fileName);

            if (!file.exists()) {
                Bukkit.getLogger().warning("File " + fileName+ " does not exist");
                continue;
            }

            ConfigurationSection config = YamlConfiguration.loadConfiguration(file).getConfigurationSection("overrides");
            for (String key : config.getKeys(false)) {

                ConfigurationSection override = config.getConfigurationSection(key);

                if (override == null || !override.getBoolean("enabled", true)) {
                    continue;
                }

                Override overrideType = new Override(
                    override.getStringList("conditions"), override.getStringList("kick")
                );
                overrides.add(overrideType);

            }

        }

    }

    public void check(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        for (Override override : overrides) {
            if (plugin.conditionManager.condition(event, override.conditions)) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, formatList(player, override.kick));
                return;
            }
        }
    }

    public record Override(
            List<String> conditions,
            List<String> kick
    ) {}

    private String formatList(Player player, List<String> list) {
        return Colorizer.get.colorize(player, String.join("\n", list));
    }

}
