package me.superchirok1.playeraccesslist.listener;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.superchirok1.playeraccesslist.PlayerAccessList;
import me.superchirok1.playeraccesslist.color.Colorizer;
import me.superchirok1.playeraccesslist.config.AccessConfig;
import me.superchirok1.playeraccesslist.events.BlacklistCancelJoinEvent;
import me.superchirok1.playeraccesslist.events.WhitelistCancelJoinEvent;
import me.superchirok1.playeraccesslist.list.ListType;
import me.superchirok1.playeraccesslist.list.impl.Blacklist;
import me.superchirok1.playeraccesslist.list.impl.Whitelist;
import me.superchirok1.playeraccesslist.util.EventPlaceholder;
import me.superchirok1.playeraccesslist.util.LogHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class MainListener implements Listener {

    private final PlayerAccessList plugin;

    public MainListener(PlayerAccessList plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;

        LogHandler logHandler = plugin.logHandler;
        AccessConfig accessConfig = plugin.accessConfig;

        var wl = accessConfig.whitelist;
        var bl = accessConfig.blacklist;
        var nick = accessConfig.nickname;

        if (bl.enable() && checkBlacklist(event)) {
            kick(bl.kick(), event);
            plugin.logHandler.logBlacklist(event);
            Bukkit.getPluginManager().callEvent(new BlacklistCancelJoinEvent(event.getPlayerProfile()));
            return;
        }

        if (wl.enable() && checkWhitelist(event)) {
            kick(wl.kick(), event);
            plugin.logHandler.logWhitelist(event);
            Bukkit.getPluginManager().callEvent(new WhitelistCancelJoinEvent(event.getPlayerProfile()));
            return;
        }

        if (nick.enable() && !checkNickname(nick.regex(), event)) {
            kick(nick.kickMessage(), event);
            plugin.logHandler.logKick(event);
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerLoginEvent event) {
        plugin.overrides.check(event);
    }

    private boolean checkBlacklist(AsyncPlayerPreLoginEvent event) {
        Blacklist bl = plugin.blacklist;
        UUID uuid = event.getUniqueId();
        String name = event.getName();
        String ip = event.getAddress().getHostAddress();

        return bl.contains(uuid)
                || bl.contains(name)
                || bl.contains(ip);
    }

    private boolean checkWhitelist(AsyncPlayerPreLoginEvent event) {
        Whitelist bl = plugin.whitelist;
        UUID uuid = event.getUniqueId();
        String name = event.getName();
        String ip = event.getAddress().getHostAddress();

        return !(bl.contains(uuid)
                || bl.contains(name)
                || bl.contains(ip));
    }

    private boolean checkNickname(Pattern pattern, AsyncPlayerPreLoginEvent event) {
        return pattern.matcher(event.getName()).matches();
    }

    private void kick(String string, AsyncPlayerPreLoginEvent event) {
        string = EventPlaceholder.parse(
                Colorizer.get.colorize(string), event
        );

        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, string);
    }

    private void kick(List<String> list, AsyncPlayerPreLoginEvent event) {
        kick(fromListToString(list), event);
    }

    private String fromListToString(List<String> list) {
        return String.join("\n", list);
    }

}
