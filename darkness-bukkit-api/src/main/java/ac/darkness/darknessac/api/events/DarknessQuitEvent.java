package ac.darkness.darknessac.api.events;

import ac.darkness.darknessac.api.DarknessUser;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Deprecated(since = "1.2.1.0", forRemoval = true)
public class DarknessQuitEvent extends Event implements DarknessUserEvent {

    private static final HandlerList handlers = new HandlerList();
    private final DarknessUser user;

    public DarknessQuitEvent(DarknessUser user) {
        super(true); // Async!
        this.user = user;
    }

    @Override
    public DarknessUser getUser() {
        return user;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
