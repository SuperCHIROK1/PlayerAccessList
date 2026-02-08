package me.superchirok1.playeraccesslist.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class EventPlaceholder {

    private static PlayerLoginEvent event;

    public static String parse(String string, AsyncPlayerPreLoginEvent event) {
        return string.replace("%player%", event.getName())
                .replace("%ip%", event.getAddress().toString())
                .replace("%has_played%", hasPlayedBefore(event))
                .replace("%uuid%", event.getUniqueId().toString())
        ;
    }

    public static String hasPlayedBefore(AsyncPlayerPreLoginEvent event) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(event.getUniqueId());
        return String.valueOf(player.hasPlayedBefore());
    }

    public static String hasPlayedBeforeLogin(PlayerLoginEvent event) {
        return String.valueOf(event.getPlayer().hasPlayedBefore());
    }

    public static String parseLogin(String string, PlayerLoginEvent event) {
        Player profile = event.getPlayer();

        return string.replace("%player%", profile.getName())
                .replace("%ip%", event.getAddress().getHostAddress())
                .replace("%has_played%", hasPlayedBeforeLogin(event))
                .replace("%uuid%", profile.getUniqueId().toString());
    }

}
