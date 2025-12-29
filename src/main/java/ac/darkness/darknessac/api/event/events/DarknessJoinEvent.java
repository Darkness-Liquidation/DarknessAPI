package ac.darkness.darknessac.api.event.events;

import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.event.DarknessEvent;

public class DarknessJoinEvent extends DarknessEvent implements DarknessUserEvent {
    private final DarknessUser user;

    public DarknessJoinEvent(DarknessUser user) {
        super(true); // Async
        this.user = user;
    }

    @Override
    public DarknessUser getUser() {
        return user;
    }
}
