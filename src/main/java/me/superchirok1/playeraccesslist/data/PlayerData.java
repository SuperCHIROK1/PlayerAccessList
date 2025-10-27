package me.superchirok1.playeraccesslist.data;

import java.util.HashSet;
import java.util.Set;

public class PlayerData {
    private Set<String> whitelist;
    private Set<String> blacklist;

    public PlayerData() {
        whitelist = new HashSet<>();
        blacklist = new HashSet<>();
    }

    public Set<String> getWhitelist() { return whitelist; }
    public Set<String> getBlacklist() { return blacklist; }
}
