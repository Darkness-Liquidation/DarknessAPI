package ac.darkness.darknessac.api.event.events;

import ac.darkness.darknessac.api.AbstractCheck;
import ac.darkness.darknessac.api.DarknessUser;

public class CompletePredictionEvent extends DarknessCheckEvent {
    private final double offset;

    public CompletePredictionEvent(DarknessUser player, AbstractCheck check, double offset) {
        super(player, check);
        this.offset = offset;
    }

    public double getOffset() {
        return offset;
    }
}
