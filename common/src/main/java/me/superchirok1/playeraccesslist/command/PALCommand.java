package me.superchirok1.playeraccesslist.command;

import me.superchirok1.playeraccesslist.PlayerAccessList;
import me.superchirok1.playeraccesslist.list.ListType;
import me.superchirok1.playeraccesslist.util.MigrateManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class PALCommand implements CommandExecutor, TabCompleter {

    private final PlayerAccessList plugin;

    public PALCommand(PlayerAccessList plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        var locale = plugin.locale.get;

        if (!commandSender.hasPermission("pal.admin")) {
            commandSender.sendMessage(locale.noPerms());
            return true;
        }

        if (strings.length == 0) {
            commandSender.sendMessage(locale.admin_usage());
            return true;
        }

        switch (strings[0]) {
            case "reload":
                long start = System.currentTimeMillis();

                plugin.reload();

                long duration = System.currentTimeMillis() - start;
                commandSender.sendMessage(String.format(locale.reloaded(), duration));
                return true;
            case "migrate":
                if (strings.length < 3) {
                    commandSender.sendMessage(locale.mUsage());
                    return true;
                }

                long mStart = System.currentTimeMillis();
                String from = strings[1].toLowerCase();
                String to = strings[2].toLowerCase();

                ListType type = to.equals("whitelist") ? ListType.WHITELIST : ListType.BLACKLIST;

                switch (from) {
                    case "json":
                        plugin.migrateManager.migrateFromJson(new File(plugin.getDataFolder(), to).toPath(), MigrateManager.MigrateMethod.ADD);
                        break;
                    case "vanilla-whitelist":
                        plugin.migrateManager.fromVanillaWhitelist(ListType.WHITELIST, MigrateManager.MigrateMethod.ADD);
                        break;
                    case "vanilla-blacklist":
                        plugin.migrateManager.fromVanillaBannedList(ListType.BLACKLIST, MigrateManager.MigrateMethod.ADD);
                        break;
                    default:
                        commandSender.sendMessage(locale.mUsage());
                        return true;
                }

                commandSender.sendMessage(String.format(locale.mSuccessful(), System.currentTimeMillis() - mStart));
                return true;

            default:
                commandSender.sendMessage(locale.admin_usage());
                return true;
        }

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.hasPermission("pal.admin")) {
            return List.of();
        }

        if (strings.length == 1) {
            return List.of("reload", "migrate");
        }

        if (strings[0].equalsIgnoreCase("migrate")) {
            if (strings.length == 2) {
                return List.of("vanilla-whitelist", "vanilla-blacklist", "json");
            }
            if (strings.length == 3) {

                if (strings[1].equalsIgnoreCase("vanilla_whitelist") || strings[1].equalsIgnoreCase("vanilla-blacklist")) {
                    return List.of("whitelist", "blacklist");
                }

                if (strings[1].equalsIgnoreCase("json")) {
                    return List.of("<file_in_plugin_folder>");
                }

            }

        }

        return List.of();
    }

}
