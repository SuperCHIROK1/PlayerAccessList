package me.superchirok1.playeraccesslist.color.impl;

import me.clip.placeholderapi.PlaceholderAPI;
import me.superchirok1.playeraccesslist.color.TextColorizer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LegacyUpdatedSerializer implements TextColorizer {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern ALT_HEX_PATTERN = Pattern.compile("<#([A-Fa-f0-9]{6})>");

    @Override
    public String colorize(String input) {
        return colorize(null, input);
    }

    @Override
    public String colorize(Player player, String input) {
        if (checkEmpty(input)) return input;

        if (player != null) {
            input = parsePlaceholders(player, input);
        }

        return parseColors(input);
    }

    private boolean checkEmpty(String input) {
        return input == null || input.isEmpty();
    }

    private String parsePlaceholders(Player player,String input) {
        return PlaceholderAPI.setPlaceholders(player, input);
    }

    private String parseHex(String input) {
        Matcher matcher = HEX_PATTERN.matcher(input);
        return getString(matcher);
    }

    private String parseAltHex(String input) {
        Matcher matcher = ALT_HEX_PATTERN.matcher(input);
        return getString(matcher);
    }

    @NotNull private String getString(Matcher matcher) {
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String hex = matcher.group(1);
            String color = ChatColor.of("#" + hex).toString();
            matcher.appendReplacement(sb, Matcher.quoteReplacement(color));
        }
        return matcher.appendTail(sb).toString();
    }

    private String parseColors(String input) {
        input = parseHex(input);
        input = parseAltHex(input);
        input = ChatColor.translateAlternateColorCodes('&', input);
        return input;
    }

}
