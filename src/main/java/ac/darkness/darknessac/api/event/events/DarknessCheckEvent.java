package ac.darkness.darknessac.api.event.events;

import ac.darkness.darknessac.api.AbstractCheck;
import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.event.Cancellable;
import lombok.Getter;

public abstract class DarknessCheckEvent extends ac.darkness.darknessac.api.event.DarknessEvent implements DarknessUserEvent, Cancellable {
    private final DarknessUser user;
    @Getter
    protected final AbstractCheck check;
    private boolean cancelled;

    public DarknessCheckEvent(DarknessUser user, AbstractCheck check) {
        super(true); // Async
        this.user = user;
        this.check = check;
    }

    @Override
    public DarknessUser getUser() {
        return user;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }

    public double getViolations() {
        return check.getViolations();
    }

    public boolean isSetback() {
        return check.getViolations() > check.getSetbackVL();
    }
}