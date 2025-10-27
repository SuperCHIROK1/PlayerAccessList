package me.superchirok1.playeraccesslist;

import me.superchirok1.playeraccesslist.lists.Blacklist;
import me.superchirok1.playeraccesslist.lists.Whitelist;

public class AccessLists {
    private final Whitelist whitelist;
    private final Blacklist blacklist;

    public AccessLists(PlayerAccessList plugin) {
        this.whitelist = new Whitelist(plugin);
        this.blacklist = new Blacklist(plugin);
    }

    public Whitelist getWhitelist() { return whitelist; }
    public Blacklist getBlacklist() { return blacklist; }

    public void reload() {
        whitelist.reload();
        blacklist.reload();
    }
}
