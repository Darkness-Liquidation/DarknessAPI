package ac.darkness.darknessac.api.events;

import ac.darkness.darknessac.api.event.DarknessEvent;
import lombok.Getter;

public class DarknessReloadEvent extends DarknessEvent {

    @Getter
    private final boolean success;

    @Getter
    private final long durationMillis;

    @Getter
    private final long reloadTime;

    public DarknessReloadEvent(boolean success) {
        this(success, 0L, System.currentTimeMillis());
    }

    public DarknessReloadEvent(boolean success, long durationMillis) {
        this(success, durationMillis, System.currentTimeMillis());
    }

    public DarknessReloadEvent(boolean success, long durationMillis, long reloadTime) {
        super(true); // Async
        this.success = success;
        this.durationMillis = durationMillis;
        this.reloadTime = reloadTime;
    }
}
