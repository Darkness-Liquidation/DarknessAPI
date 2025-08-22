package ac.darkness.darknessac.api.event.events;

import ac.darkness.darknessac.api.DarknessUser;

public interface DarknessUserEvent {
    DarknessUser getUser();
    default DarknessUser getPlayer() {
        return getUser();
    }
}

