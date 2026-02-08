package me.superchirok1.playeraccesslist.list.impl;

import lombok.Getter;
import me.superchirok1.playeraccesslist.PlayerAccessList;
import me.superchirok1.playeraccesslist.list.AccessList;
import me.superchirok1.playeraccesslist.list.ListType;

public class Blacklist extends AccessList {

    private final PlayerAccessList plugin;
    @Getter private static Blacklist instance;

    public Blacklist(PlayerAccessList plugin) {
        super(plugin, ListType.BLACKLIST);
        this.plugin = plugin;
        instance = this;
    }

}
