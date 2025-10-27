package me.superchirok1.playeraccesslist.util;

import me.clip.placeholderapi.PlaceholderAPI;
import me.superchirok1.playeraccesslist.PlayerAccessList;
import me.superchirok1.playeraccesslist.config.MessagesConfig;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Text {

    private static PlayerAccessList plugin;
    private static BukkitAudiences audiences;
    private static final MiniMessage MM = MiniMessage.miniMessage();

    private static final Pattern HEX_AMP = Pattern.compile("(?i)&#([0-9a-f]{6})");
    private static final Pattern LEGACY = Pattern.compile("(?i)[&§]([0-9A-FK-OR])");

    public static void init(BukkitAudiences a) { audiences = a; }

    public static void send(CommandSender sender, String message) {
        audiences.sender(sender).sendMessage(format(sender, message));
    }

    public static Component format(CommandSender sender, String message) {
        if (message == null) return Component.empty();

        if (sender instanceof Player p && Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            message = PlaceholderAPI.setPlaceholders(p, message);
        }

        String prefix = new MessagesConfig(plugin).getPrefix();
        if (prefix != null) {
            message = message.replace("{PRFX}", prefix);
        }

        String converted = legacyAndHexToMiniMessage(message);

        return MM.deserialize(converted);
    }

    public static Component format(String message) {
        return format(null, message);
    }

    private static String legacyAndHexToMiniMessage(String input) {
        if (input == null || input.isEmpty()) return "";

        String out = HEX_AMP.matcher(input).replaceAll("<#$1>");

        Matcher m = LEGACY.matcher(out);
        StringBuffer sb = new StringBuffer();

        boolean bold = false, italic = false, underlined = false, strikethrough = false, obfuscated = false;

        while (m.find()) {
            char c = Character.toLowerCase(m.group(1).charAt(0));
            String rep = "";

            switch (c) {
                case '0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f' -> {
                    rep = "<reset>" + switch (c) {
                        case '0' -> "<black>"; case '1' -> "<dark_blue>"; case '2' -> "<dark_green>";
                        case '3' -> "<dark_aqua>"; case '4' -> "<dark_red>"; case '5' -> "<dark_purple>";
                        case '6' -> "<gold>"; case '7' -> "<gray>"; case '8' -> "<dark_gray>";
                        case '9' -> "<blue>"; case 'a' -> "<green>"; case 'b' -> "<aqua>";
                        case 'c' -> "<red>"; case 'd' -> "<light_purple>"; case 'e' -> "<yellow>";
                        case 'f' -> "<white>";
                        default -> "";
                    };
                    bold = italic = underlined = strikethrough = obfuscated = false;
                }

                case 'k' -> { rep = "<obfuscated>"; obfuscated = true; }
                case 'l' -> { rep = "<bold>"; bold = true; }
                case 'm' -> { rep = "<strikethrough>"; strikethrough = true; }
                case 'n' -> { rep = "<underlined>"; underlined = true; }
                case 'o' -> { rep = "<italic>"; italic = true; }

                case 'r' -> {
                    rep = "<reset>";
                    bold = italic = underlined = strikethrough = obfuscated = false;
                }
            }

            m.appendReplacement(sb, Matcher.quoteReplacement(rep));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static BukkitAudiences getAudiences() { return audiences; }

    private static final LegacyComponentSerializer LEGACY_SERIALIZER = LegacyComponentSerializer.builder()
            .character('\u00A7')
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    public static String formatToString(CommandSender sender, String message) {
        if (message == null) return "";
        Component comp = format(sender, message);
        return LEGACY_SERIALIZER.serialize(comp);
    }

    public static String formatToString(String message) {
        return formatToString(null, message);
    }

    public static void setPlugin(PlayerAccessList plugin) {
        Text.plugin = plugin;
    }
}
