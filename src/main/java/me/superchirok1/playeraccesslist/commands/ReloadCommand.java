package me.superchirok1.playeraccesslist.commands;

import me.superchirok1.playeraccesslist.PlayerAccessList;
import me.superchirok1.playeraccesslist.util.Text;
import me.superchirok1.playeraccesslist.config.MessagesConfig;
import me.superchirok1.playeraccesslist.data.JSONData;
import me.superchirok1.playeraccesslist.data.YAMLData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private final PlayerAccessList plugin;

    public ReloadCommand(PlayerAccessList plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("pal.admin")) {
            Text.send(sender, new MessagesConfig(plugin).getNoPerms());
            return true;
        }

        if (args.length == 0) {
            Text.send(sender, """
                    &f
                    &a&lPlayerAccessList
                    &7&m      &f
                    &fКоманды:
                     &a/pal <reload>
                     &a/whitelist <toggle/add/remove/list>
                     &a/blacklist <toggle/add/remove/list>
                     &f
                    <click:open_url:'https://spigotmc.ru/resources/playeraccesslist-belyj-chernyj-spisok.4243/'><hover:show_text:'<green>Открыть ссылку'><green><bold>[SpigotMC.ru]</bold></green>
                    """);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            new JSONData(plugin.getDataFolder()).loadData();
            new YAMLData(plugin.getDataFolder()).reload();
            plugin.reload();

            Text.send(sender, new MessagesConfig(plugin).getReloaded());
            return true;
        }

        return true;

    }
}
