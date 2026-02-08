package me.superchirok1.playeraccesslist.util;

import com.google.gson.Gson;
import me.superchirok1.playeraccesslist.PlayerAccessList;
import me.superchirok1.playeraccesslist.list.ListType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public class MigrateManager {

    private final PlayerAccessList plugin;

    public MigrateManager(PlayerAccessList plugin) {
        this.plugin = plugin;
    }

    public void fromVanillaWhitelist(ListType type, MigrateMethod migrateMethod) {

        Set<OfflinePlayer> vanillaWhitelist = plugin.getServer().getWhitelistedPlayers();
        if (migrateMethod == MigrateMethod.ADD) {
            vanillaWhitelist.forEach(plugin.getList(type)::add);
        }

    }

    public void fromVanillaBannedList(ListType type, MigrateMethod migrateMethod) {

        Set<OfflinePlayer> bannedlist = plugin.getServer().getBannedPlayers();
        if (migrateMethod == MigrateMethod.ADD) {
            bannedlist.forEach(plugin.getList(type)::add);
        }

    }

    public void migrateFromJson(Path path, MigrateMethod migrateMethod) {

        Gson gson = new Gson();

        try (FileReader reader = new FileReader(path.toFile())) {

            OldJsonAccessList oldjson = gson.fromJson(reader, OldJsonAccessList.class);

            if (oldjson.getWhitelist()!=null) {
                oldjson.getWhitelist().forEach(plugin.whitelist::add);
            }

            if (oldjson.getBlacklist()!=null) {
                oldjson.getBlacklist().forEach(plugin.blacklist::add);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public enum MigrateMethod {ADD}

}
