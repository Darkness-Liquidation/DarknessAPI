package ac.darkness.darknessac.api.event;

import lombok.Getter;

public abstract class DarknessEvent implements Cancellable {
    private boolean cancelled = false;
    @Getter private final boolean async;

    protected DarknessEvent() {
        this(false); // Default to sync
    }

    protected DarknessEvent(boolean async) {
        this.async = async;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        if (!isCancellable()) {
            throw new IllegalStateException("Event " + getEventName() + " is not cancellable");
        }
        this.cancelled = cancelled;
    }

    public boolean isCancellable() {
        return false; // Override in cancellable events
    }

    public String getEventName() {
        return getClass().getSimpleName();
    }
}
