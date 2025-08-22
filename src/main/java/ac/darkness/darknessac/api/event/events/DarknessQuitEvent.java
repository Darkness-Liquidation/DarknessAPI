package ac.darkness.darknessac.api.event.events;

import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.event.DarknessEvent;

public class DarknessQuitEvent extends DarknessEvent implements DarknessUserEvent {
    private final DarknessUser user;

    public DarknessQuitEvent(DarknessUser user) {
        super(true); // Async
        this.user = user;
    }

    @Override
    public DarknessUser getUser() {
        return user;
    }
}
