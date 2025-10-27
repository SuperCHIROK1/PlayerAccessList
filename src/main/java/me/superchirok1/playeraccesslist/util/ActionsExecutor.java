package me.superchirok1.playeraccesslist.util;

import me.superchirok1.playeraccesslist.PlayerAccessList;
import me.superchirok1.playeraccesslist.config.MainConfig;
import org.bukkit.Bukkit;

import java.util.List;

public class ActionsExecutor {

    private final PlayerAccessList plugin;
    private final MainConfig cfg;

    public ActionsExecutor(PlayerAccessList plugin) {
        this.cfg = new MainConfig(plugin);
        this.plugin = plugin;
    }

    public void blacklistOnEntry() {
        execute(cfg.getBlacklistCommandsOnEntry());
    }

    public void blacklistOnKick() {
        execute(cfg.getBlacklistCommandsOnKick());
    }

    public void whitelistOnKick() {
        execute(cfg.getWhitelistCommandsOnKick());
    }

    public void whitelistOnEntry() {
        execute(cfg.getWhitelistCommandsOnEntry());
    }

    public void execute(List<String> cmds) {
        if (!cmds.isEmpty()) {
            for (String cmd : cmds) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }
        }
    }

}
