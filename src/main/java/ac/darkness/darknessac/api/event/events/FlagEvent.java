package ac.darkness.darknessac.api.event.events;

import ac.darkness.darknessac.api.AbstractCheck;
import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.event.Cancellable;

public class FlagEvent extends DarknessVerboseCheckEvent {

    public FlagEvent(DarknessUser user, AbstractCheck check, String verbose) {
        super(user, check, verbose);
    }
}
