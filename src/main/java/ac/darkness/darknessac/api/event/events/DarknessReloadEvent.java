package ac.darkness.darknessac.api.event.events;

public class DarknessReloadEvent extends ac.darkness.darknessac.api.event.DarknessEvent {
    private final boolean success;

    public DarknessReloadEvent(boolean success) {
        super(true); // Async
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
