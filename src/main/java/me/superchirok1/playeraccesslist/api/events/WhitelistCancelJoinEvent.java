package me.superchirok1.playeraccesslist.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WhitelistCancelJoinEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Player player;

    public WhitelistCancelJoinEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
