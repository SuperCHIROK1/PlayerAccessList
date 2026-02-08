package me.superchirok1.playeraccesslist.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.superchirok1.playeraccesslist.PlayerAccessList;
import me.superchirok1.playeraccesslist.list.ListType;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceholderHook extends PlaceholderExpansion {

    private final PlayerAccessList plugin;
    public PlaceholderHook(PlayerAccessList plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "pal";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        var wl = plugin.whitelist;
        String value="PAL v"+getVersion();

        switch (params) {
            case "in_whitelist":
                value = String.valueOf(
                        wl.contains(player.getUniqueId()) || wl.contains(player.getName())
                );
            case "in_blacklist":
                value = String.valueOf(
                        wl.contains(player.getUniqueId()) || wl.contains(player.getName())
                );
        }

        return value;
    }

}
