package me.superchirok1.playeraccesslist.util;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class OldJsonAccessList {
    private Set<String> whitelist;
    private Set<String> blacklist;
}
