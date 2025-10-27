package me.superchirok1.playeraccesslist.commands;

import me.superchirok1.playeraccesslist.PlayerAccessList;
import me.superchirok1.playeraccesslist.config.MainConfig;
import me.superchirok1.playeraccesslist.config.MessagesConfig;
import me.superchirok1.playeraccesslist.lists.Blacklist;
import me.superchirok1.playeraccesslist.lists.Whitelist;
import me.superchirok1.playeraccesslist.util.CheckInList;
import me.superchirok1.playeraccesslist.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlacklistCommand implements CommandExecutor, TabCompleter {

    private final PlayerAccessList plugin;
    private final MainConfig cfg;
    private final MessagesConfig msgs;
    private final Blacklist list;

    public BlacklistCommand(PlayerAccessList plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getConf();
        this.msgs = plugin.getMessagesConf();
        this.list = plugin.getAccessLists().getBlacklist();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender.hasPermission("pal.admin")
                || sender.hasPermission("pal.blacklist"))) {
            Text.send(sender, msgs.getNoPerms());
            return true;
        }

        if (args.length == 0) {
            String msg = msgs.getUsage().replace("{command}", "/blacklist <toggle/list/add/remove>");
            Text.send(sender, msg);
            return true;
        }

        List<String> kickMessage = cfg.getBlacklistMessage();
        String message = String.join("\n", kickMessage);
        CheckInList checker = new CheckInList(plugin);

        switch (args[0].toLowerCase()) {

            case "toggle" -> {

                boolean enabled = cfg.toggleBl();

                String msg = msgs.getSwitchedTo()
                        .replace("{list}", msgs.getPhBlacklist())
                        .replace("{boolean}", enabled ? msgs.getPhTrue() : msgs.getPhFalse());
                Text.send(sender, msg);

                if (enabled) {
                    plugin.getServer().getOnlinePlayers().forEach(player -> {
                        if (list.has(player.getName()) || list.has(player.getUniqueId().toString())) {
                            player.kickPlayer(Text.formatToString(player, message));
                        }
                    });
                }
                return true;

            }

            case "remove" -> {

                if (args.length == 2) {
                    String name = args[1];
                    if (list.has(name)) {
                        list.remove(name);
                        String msg = msgs.getRemoved()
                                .replace("{plr}", name)
                                .replace("{list}", msgs.getPhBlacklist());
                        Text.send(sender, msg);
                        checker.checkAndKick(Bukkit.getPlayer(name));
                    } else {
                        Text.send(sender, msgs.getNoSuchThing());
                    }
                } else {
                    String msg = msgs.getUsage()
                            .replace("{command}", "/blacklist remove <ник>");
                    Text.send(sender, msg);
                }
                return true;

            }

            case "add" -> {

                if (args.length == 2) {
                    String name = args[1];
                    if (!list.has(name)) {
                        list.add(name);
                        String msg = msgs.getAdded()
                                .replace("{plr}", name)
                                .replace("{list}", msgs.getPhBlacklist());
                        Text.send(sender, msg);
                        checker.checkAndKick(Bukkit.getPlayer(name));
                    } else {
                        Text.send(sender, msgs.getSuchAlreadyExists());
                    }
                } else {
                    String msg = msgs.getUsage()
                            .replace("{command}", "/blacklist add <ник>");
                    Text.send(sender, msg);
                }
                return true;

            }

            case "list" -> {

                Text.send(sender, msgs.getPrefix() + " &7В списке:");
                for (String name : list.getList()) {
                    String msg = msgs.getPrefix() + " &8- &f{name}".replace("{name}", name);
                    Text.send(sender, msg);
                }
                return true;

            }

            case "status" -> {

                String enabled;

                if (cfg.isBlacklistEnabled()) {
                    enabled = msgs.getPhTrue();
                } else {
                    enabled = msgs.getPhFalse();
                }

                Text.send(sender, msgs.getStatus().replace("{boolean}", enabled));
                return true;

            }

            default -> {
                String msg = msgs.getUsage().replace("{command}", "/blacklist <toggle/list/add/remove>");
                Text.send(sender, msg);
                return true;
            }

        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String alias,
                                                @NotNull String[] args) {

        if (sender.hasPermission("pal.admin")
                || sender.hasPermission("pal.blacklist")) {

            if (args.length == 1) {
                return List.of("toggle", "list", "add", "remove", "status");
            }

            if (args.length == 2) {
                return switch (args[0].toLowerCase()) {
                    case "add" -> plugin.getServer().getOnlinePlayers().stream()
                            .map(p -> p.getName())
                            .toList();
                    case "remove" -> list.getList();
                    default -> List.of();
                };
            }
        }
        return List.of();
    }
}
