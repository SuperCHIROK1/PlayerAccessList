package me.superchirok1.playeraccesslist.command;

import me.superchirok1.playeraccesslist.PlayerAccessList;
import me.superchirok1.playeraccesslist.color.Colorizer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class WhitelistCommand implements CommandExecutor, TabCompleter {

    private final PlayerAccessList plugin;

    public WhitelistCommand(PlayerAccessList plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        var config = plugin.locale.get;
        var access = plugin.accessConfig;
        var whitelist = plugin.whitelist;

        if (!(commandSender.hasPermission("pal.whitelist") || commandSender.hasPermission("pal.admin"))) {
            commandSender.sendMessage(config.noPerms());
            return true;
        }

        if (strings.length == 0) {
            commandSender.sendMessage(String.format(config.usage(), "/whitelist <add|remove|toggle|status>"));
            return true;
        }

        switch (strings[0]) {
            case "add" -> {
                if (strings.length < 2) {
                    commandSender.sendMessage(String.format(config.usage(), "/whitelist add <name_or_uuid_or_ip>"));
                    return true;
                }

                if (whitelist.contains(strings[1])) {
                    commandSender.sendMessage(String.format(config.wExists(), strings[1]));
                    return true;
                }

                if (access.whitelist.cannotBeAdded().contains(strings[1])) {
                    commandSender.sendMessage(config.wCannotBeAdded());
                    return true;
                }

                whitelist.add(strings[1]);
                commandSender.sendMessage(String.format(config.wAdded(), strings[1]));
                return true;
            }
            case "remove" -> {
                if (strings.length < 2) {
                    commandSender.sendMessage(String.format(config.usage(), "/whitelist remove <name_or_uuid_or_ip>"));
                    return true;
                }

                Player player = Bukkit.getPlayer(strings[1]);
                if (access.whitelist.enable() && player != null) {
                    player.kickPlayer(Colorizer.get.colorize(player, String.join("\n",
                            access.whitelist.kick())));
                }

                whitelist.remove(strings[1]);
                commandSender.sendMessage(String.format(config.wRemoved(), strings[1]));
                return true;
            }
            case "toggle" -> {

                access.toggleWhitelist(plugin, !access.whitelist.enable());
                if (access.whitelist.enable()) {
                    String kick = Colorizer.get.colorize(String.join("\n", access.whitelist.kick()));
                    plugin.getServer().getOnlinePlayers().forEach(player -> {
                        if (!whitelist.contains(player.getName())
                                && !whitelist.contains(player.getUniqueId().toString())&&
                                !whitelist.contains(player.getAddress().getAddress().getHostAddress())
                        ) {
                            player.kickPlayer(kick);
                        }
                    });
                }
                commandSender.sendMessage(String.format(config.wStatus(), access.whitelist.enable()
                        ? config.pTrue() : config.pFalse()));
                return true;

            }
            case "status" -> {

                boolean enabled = access.whitelist.enable();
                String value = enabled ? config.pTrue() : config.pFalse();

                commandSender.sendMessage(String.format(config.wStatus(), value));
                return true;

            }
            default -> {
                commandSender.sendMessage(String.format(config.usage(), "/whitelist <add|remove|toggle|status>"));
                return true;
            }
        }

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender.hasPermission("pal.whitelist") || commandSender.hasPermission("pal.admin"))) {
            return List.of();
        }

        if (strings.length == 1) {
            return List.of("add", "remove", "toggle", "status");
        }

        if (strings.length == 2) {
            if (strings[0].equalsIgnoreCase("add") || strings[0].equalsIgnoreCase("remove")) {
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(strings[1].toLowerCase()))
                        .collect(Collectors.toList());
            }
        }

        return List.of();
    }

}
