package me.superchirok1.playeraccesslist.condition.impl;

import me.superchirok1.playeraccesslist.condition.Condition;
import org.bukkit.event.player.PlayerLoginEvent;

public class UUIDCondition implements Condition {
    @Override
    public String prefix() {
        return "uuid";
    }

    @Override
    public boolean condition(String text, PlayerLoginEvent event) {
        return event.getPlayer().getUniqueId().toString().equalsIgnoreCase(text);
    }
}
