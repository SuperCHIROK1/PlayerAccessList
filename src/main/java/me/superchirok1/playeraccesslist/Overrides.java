package me.superchirok1.playeraccesslist;

import me.superchirok1.playeraccesslist.config.OverridesConfig;
import me.superchirok1.playeraccesslist.config.OverridesSection;
import me.superchirok1.playeraccesslist.util.Text;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Objects;

public class Overrides {

    private final PlayerAccessList plugin;

    public Overrides(PlayerAccessList plugin) {
        this.plugin = plugin;
    }

    public void check(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        OverridesConfig config = new OverridesConfig(plugin);

        for (String key : config.getOverrides().getKeys(false)) {
            OverridesSection section = new OverridesSection(
                    Objects.requireNonNull(config.getConfig().getConfigurationSection("overrides." + key))
            );

            if (!section.isEnabled()) continue;

            if (matches(player.getName(), section.getName())
                    || matches(player.getWorld().getName(), section.getWorld()) || matches(player.getUniqueId().toString(), section.getUuid())
                    || matchesPermission(player, section.getPermission())) {

                event.disallow(PlayerLoginEvent.Result.KICK_OTHER,
                        Text.formatToString(player, String.join("\n", section.getMessage())));
                return;
            }
        }
    }

    private boolean matches(String value, String target) {
        if (target == null || target.isEmpty()) return false;

        if (target.startsWith("!")) {
            return !value.equalsIgnoreCase(target.substring(1));
        } else {
            return value.equalsIgnoreCase(target);
        }
    }

    private boolean matchesPermission(Player player, String permission) {
        if (permission == null || permission.isEmpty()) return false;

        if (permission.startsWith("!")) {
            return !player.hasPermission(permission.substring(1));
        } else {
            return player.hasPermission(permission);
        }
    }

}
