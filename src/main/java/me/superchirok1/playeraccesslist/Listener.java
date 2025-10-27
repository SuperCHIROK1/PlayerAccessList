package me.superchirok1.playeraccesslist;

import me.superchirok1.playeraccesslist.api.events.BlacklistCancelJoinEvent;
import me.superchirok1.playeraccesslist.api.events.WhitelistCancelJoinEvent;
import me.superchirok1.playeraccesslist.config.MainConfig;
import me.superchirok1.playeraccesslist.config.MessagesConfig;
import me.superchirok1.playeraccesslist.util.ActionsExecutor;
import me.superchirok1.playeraccesslist.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.List;

public class Listener implements org.bukkit.event.Listener {

    private final PlayerAccessList plugin;

    public Listener(PlayerAccessList plugin) {
        this.plugin = plugin;
    }

    private String formatList(List<String> list) {
        return String.join("\n", list);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerLoginEvent event) {

        new Overrides(plugin).check(event);
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }

        MainConfig cfg = new MainConfig(plugin);
        Player player = event.getPlayer();
        ActionsExecutor ae = new ActionsExecutor(plugin);

        String ip = "???";
        if (player.getAddress() != null && player.getAddress().getAddress() != null) {
            ip = player.getAddress().getAddress().getHostAddress();
        }

        if (cfg.isBlacklistEnabled()) {
            for (String value : plugin.getAccessLists().getBlacklist().getList()) {
                if (player.getName().equalsIgnoreCase(value)
                        || player.getUniqueId().toString().equalsIgnoreCase(value)) {
                    event.setKickMessage(Text.formatToString(player, formatList(cfg.getBlacklistMessage())));
                    event.setResult(PlayerLoginEvent.Result.KICK_BANNED);
                    logBlacklist(player.getName(), ip);
                    ae.blacklistOnKick();
                    Bukkit.getPluginManager().callEvent(new BlacklistCancelJoinEvent(player));
                    return;
                }
                ae.blacklistOnEntry();
            }
        }

        if (cfg.isWhitelistEnabled()) {
            List<String> whitelist = plugin.getAccessLists().getWhitelist().getList();
            boolean allowed = whitelist.stream()
                    .anyMatch(s -> s.equalsIgnoreCase(player.getName())
                            || s.equalsIgnoreCase(player.getUniqueId().toString()));

            if (!allowed) {
                event.setKickMessage(Text.formatToString(player, formatList(cfg.getWhitelistMessage())));
                event.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
                logWhitelist(player.getName(), ip);
                ae.whitelistOnKick();
                Bukkit.getPluginManager().callEvent(new WhitelistCancelJoinEvent(player));
                return;
            }
            ae.whitelistOnEntry();
        }



    }

    private void logKick(String name, String ip) {
        log(name, ip, new MessagesConfig(plugin).getKickLog());
    }

    private void logBlacklist(String name, String ip) {
        log(name, ip, new MessagesConfig(plugin).getKickLogBl());
    }

    private void logWhitelist(String name, String ip) {
        log(name, ip, new MessagesConfig(plugin).getKickLogWl());
    }

    private void log(String name, String ip, String value) {
        if (value == null)
            return;

        String formatted = value.replace("{plr}", name).replace("{ip}", ip);

        if (new MainConfig(plugin).isLogConsole()) {
            Bukkit.getConsoleSender().sendMessage(Text.formatToString(formatted));
        }
        if (new MainConfig(plugin).isLogAdmin()){
            for (Player admin : Bukkit.getOnlinePlayers()) {
                if (admin.hasPermission("pal.admin.log") || admin.hasPermission("pal.admin"))
                    Text.send(admin, formatted);
            }
        }
    }

}
