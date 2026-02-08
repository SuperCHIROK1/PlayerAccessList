package me.superchirok1.playeraccesslist.list;

import me.superchirok1.playeraccesslist.PlayerAccessList;
import me.superchirok1.playeraccesslist.cache.CacheProvider;
import me.superchirok1.playeraccesslist.database.Database;
import me.superchirok1.playeraccesslist.database.DatabaseType;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class AccessList {

    private final PlayerAccessList plugin;
    private final Database database;
    private final CacheProvider cacheProvider;
    private final ListType listType;

    protected AccessList(PlayerAccessList plugin, ListType listType) {
        this.plugin = plugin;
        this.database = plugin.database;
        this.cacheProvider = plugin.cacheProvider;
        this.listType = listType;
    }

    public AccessList add(String value) {
        database.add(value, listType);
        cacheProvider.add(value, listType);
        return this;
    }
    public AccessList add(Player player) {
        return add(player.getUniqueId());
    }
    public AccessList add(UUID uuid) {
        return add(uuid.toString());
    }
    public AccessList add(OfflinePlayer player) {
        return add(player.getUniqueId());
    }

    public AccessList remove(String value) {
        database.remove(value, listType);
        cacheProvider.remove(value, listType);
        return this;
    }
    public AccessList remove(Player player) {
        return remove(player.getUniqueId());
    }
    public AccessList remove(UUID uuid) {
        return remove(uuid.toString());
    }
    public AccessList remove(OfflinePlayer player) {
        return remove(player.getUniqueId());
    }

    public boolean contains(String value) {
        return cacheProvider.has(value, listType);
    }
    public boolean contains(Player player) {
        return contains(player.getUniqueId());
    }
    public boolean contains(UUID uuid) {
        return contains(uuid.toString());
    }
    public boolean contains(OfflinePlayer player) {
        return contains(player.getUniqueId());
    }

}
