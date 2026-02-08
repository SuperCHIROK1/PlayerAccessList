package me.superchirok1.playeraccesslist.condition;

import me.superchirok1.playeraccesslist.condition.impl.*;
import me.superchirok1.playeraccesslist.util.EventPlaceholder;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConditionManager {

    private final Map<String, Condition> conditions = new HashMap<>();

    public void init() {
        register(new NicknameCondition());
        register(new IPCondition());
        register(new PermissionCondition());
        register(new UUIDCondition());
        register(new WorldCondition());
    }

    public void register(Condition condition) {
        conditions.put(condition.prefix().toLowerCase(), condition);
    }

    public boolean condition(PlayerLoginEvent event, List<String> conditionsList) {
        for (String condition : conditionsList) {
            boolean reversed = false;

            if (condition.startsWith("!")) {
                reversed = true;
                condition = condition.substring(1);
            }

            boolean matched = false;

            for (Condition c : conditions.values()) {

                String prefix = c.prefix().toLowerCase() + ":";
                if (!condition.toLowerCase().startsWith(prefix)) continue;

                String arg = condition.substring(prefix.length()).trim();
                boolean result = c.condition(EventPlaceholder.parseLogin(arg, event), event);

                if (reversed) result = !result;

                matched = result;
                break;

            }

            if (!matched) return false;
        }

        return true;
    }


}
