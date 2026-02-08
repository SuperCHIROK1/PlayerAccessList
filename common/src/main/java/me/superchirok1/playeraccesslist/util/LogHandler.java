package me.superchirok1.playeraccesslist.util;

import me.superchirok1.playeraccesslist.PlayerAccessList;
import me.superchirok1.playeraccesslist.config.LogConfig;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class LogHandler {

    private final PlayerAccessList plugin;

    public LogHandler(PlayerAccessList plugin) {
        this.plugin = plugin;
    }

    private void sendModeratorMessage(AsyncPlayerPreLoginEvent event, String message) {

        var log = plugin.logConfig.get;
        if (!log.moderator()) return;

        LogConfig.MessageType type = log.messageType();
        String sound = log.sound();
        String[] sounds = null;

        if (sound != null && !sound.isEmpty()) {
            sounds = sound.split(" ");
        }

        message = EventPlaceholder.parse(message, event);
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (!player.hasPermission("pal.log")) continue;

            if (type == LogConfig.MessageType.CHAT_MESSAGE) {
                player.sendMessage(message);
            } else {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
            }

            if (sounds != null) {
                player.playSound(player.getLocation(), sounds[0], Float.parseFloat(sounds[1]), Float.parseFloat(sounds[2]));
            }

        }

    }

    private void sendConsole(AsyncPlayerPreLoginEvent event, String message) {
        var log = plugin.logConfig.get;
        if (!log.console()) return;

        message = EventPlaceholder.parse(message, event);
        plugin.getServer().getConsoleSender().sendMessage(message);
    }

    public void logBlacklist(AsyncPlayerPreLoginEvent event) {
        var log = plugin.logConfig.get;
        sendConsole(event, log.blacklist());
        sendModeratorMessage(event, log.blacklist());
    }

    public void logWhitelist(AsyncPlayerPreLoginEvent event) {
        var log = plugin.logConfig.get;
        sendConsole(event, log.whitelist());
        sendModeratorMessage(event, log.whitelist());
    }

    public void logKick(AsyncPlayerPreLoginEvent event) {
        var log = plugin.logConfig.get;
        sendConsole(event, log.kick());
        sendModeratorMessage(event, log.kick());
    }

}
