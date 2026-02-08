package me.superchirok1.playeraccesslist.condition.impl;

import me.superchirok1.playeraccesslist.condition.Condition;
import org.bukkit.event.player.PlayerLoginEvent;

public class NicknameCondition implements Condition {
    @Override
    public String prefix() {
        return "nickname";
    }

    @Override
    public boolean condition(String text, PlayerLoginEvent event) {
        return event.getPlayer().getName().equals(text);
    }
}
