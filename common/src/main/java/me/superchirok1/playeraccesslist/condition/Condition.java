package me.superchirok1.playeraccesslist.condition;

import org.bukkit.event.player.PlayerLoginEvent;

public interface Condition {
    String prefix();
    boolean condition(String text, PlayerLoginEvent event);
}
