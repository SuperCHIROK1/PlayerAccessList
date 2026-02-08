package me.superchirok1.playeraccesslist.list.impl;

import lombok.Getter;
import me.superchirok1.playeraccesslist.PlayerAccessList;
import me.superchirok1.playeraccesslist.list.AccessList;
import me.superchirok1.playeraccesslist.list.ListType;

public class Whitelist extends AccessList {

    private final PlayerAccessList plugin;
    @Getter private static Whitelist instance;

    public Whitelist(PlayerAccessList plugin) {
        super(plugin, ListType.WHITELIST);
        this.plugin = plugin;
        instance = this;
    }

}
