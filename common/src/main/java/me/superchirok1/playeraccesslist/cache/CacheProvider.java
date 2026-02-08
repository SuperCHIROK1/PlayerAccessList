package me.superchirok1.playeraccesslist.cache;

import lombok.Setter;
import me.superchirok1.playeraccesslist.list.ListType;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CacheProvider {

    @Setter public Set<String> whitelist = ConcurrentHashMap.newKeySet();
    @Setter public Set<String> blacklist = ConcurrentHashMap.newKeySet();

    public void add(String value, ListType type) {
        if (type == ListType.WHITELIST) {
            whitelist.add(value);
        } else {
            blacklist.add(value);
        }
    }

    public void remove(String value, ListType type) {
        if (type == ListType.WHITELIST) {
            whitelist.remove(value);
        } else {
            blacklist.remove(value);
        }
    }

    public boolean has(String value, ListType type) {
        if (type == ListType.WHITELIST) {
            return whitelist.contains(value);
        } else {
            return blacklist.contains(value);
        }
    }

    public void clear() {
        whitelist.clear();
        blacklist.clear();
    }

}
