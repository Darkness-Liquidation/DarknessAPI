package ac.darkness.darknessac.api.event.events;

import ac.darkness.darknessac.api.event.DarknessEvent;

public class DarknessReloadEvent extends DarknessEvent {
    private final boolean success;

    public DarknessReloadEvent(boolean success) {
        super(true); // Async
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
