package me.superchirok1.playeraccesslist.events;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BlacklistCancelJoinEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final PlayerProfile player;

    public BlacklistCancelJoinEvent(PlayerProfile player) {
        super(true);
        this.player = player;
    }

    public PlayerProfile getPlayer() {
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
