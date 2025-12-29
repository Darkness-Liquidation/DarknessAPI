package ac.darkness.darknessac.api.event.events;

import ac.darkness.darknessac.api.AbstractCheck;
import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.event.Cancellable;

public abstract class DarknessVerboseCheckEvent extends DarknessCheckEvent {
    private final String verbose;

    public DarknessVerboseCheckEvent(DarknessUser user, AbstractCheck check, String verbose) {
        super(user, check);
        this.verbose = verbose;
    }

    public String getVerbose() {
        return verbose;
    }
}