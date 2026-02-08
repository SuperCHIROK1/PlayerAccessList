package me.superchirok1.playeraccesslist.condition.impl;

import me.superchirok1.playeraccesslist.condition.Condition;
import org.bukkit.event.player.PlayerLoginEvent;

public class IPCondition implements Condition {
    @Override
    public String prefix() {
        return "ip";
    }

    @Override
    public boolean condition(String text, PlayerLoginEvent event) {
        return event.getAddress().toString().equalsIgnoreCase(text);
    }
}
