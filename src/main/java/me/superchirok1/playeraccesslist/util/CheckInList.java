package me.superchirok1.playeraccesslist.util;

import me.superchirok1.playeraccesslist.Overrides;
import me.superchirok1.playeraccesslist.PlayerAccessList;
import me.superchirok1.playeraccesslist.config.MainConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.List;

public class CheckInList {

    private final PlayerAccessList plugin;

    public CheckInList(PlayerAccessList plugin) {
        this.plugin = plugin;
    }

    public void checkAndKick(Player player) {

        MainConfig cfg = new MainConfig(plugin);

        if (cfg.isBlacklistEnabled()) {
            for (String value : plugin.getAccessLists().getBlacklist().getList()) {
                if (player.getName().equalsIgnoreCase(value)
                        || player.getUniqueId().toString().equalsIgnoreCase(value)) {
                    player.kickPlayer(Text.formatToString(player, String.join("\n", cfg.getBlacklistMessage())));
                    return;
                }
            }
        }

        if (cfg.isWhitelistEnabled()) {
            List<String> whitelist = plugin.getAccessLists().getWhitelist().getList();
            boolean allowed = whitelist.stream()
                    .anyMatch(s -> s.equalsIgnoreCase(player.getName())
                            || s.equalsIgnoreCase(player.getUniqueId().toString()));

            if (!allowed) {
                player.kickPlayer(Text.formatToString(player, String.join("\n", cfg.getWhitelistMessage())));
                return;
            }

        }

    }

}
