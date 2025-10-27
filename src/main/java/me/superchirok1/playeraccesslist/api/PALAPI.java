package me.superchirok1.playeraccesslist.api;

import me.superchirok1.playeraccesslist.PlayerAccessList;
import me.superchirok1.playeraccesslist.lists.Blacklist;
import me.superchirok1.playeraccesslist.lists.Whitelist;

public class PALAPI {

    private final PlayerAccessList pl;

    public PALAPI(PlayerAccessList pl) {
        this.pl = pl;
    }

    public Whitelist getWhitelist() {
        return pl.getAccessLists().getWhitelist();
    }

    public Blacklist getBlacklist() {
        return pl.getAccessLists().getBlacklist();
    }

}
