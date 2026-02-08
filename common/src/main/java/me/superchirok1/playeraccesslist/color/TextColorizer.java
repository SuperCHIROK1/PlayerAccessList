package me.superchirok1.playeraccesslist.color;

import org.bukkit.entity.Player;

public interface TextColorizer {
    String colorize(String text);
    String colorize(Player player, String text);
}
