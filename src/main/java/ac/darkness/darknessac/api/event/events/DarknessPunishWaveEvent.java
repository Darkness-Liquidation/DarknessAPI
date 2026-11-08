package ac.darkness.darknessac.api.event.events;

import ac.darkness.darknessac.api.event.Cancellable;
import ac.darkness.darknessac.api.event.DarknessEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

public class DarknessPunishWaveEvent extends DarknessEvent implements Cancellable {
    private final Collection<UUID> punishedPlayers;
    private boolean cancelled = false;

    public DarknessPunishWaveEvent(@NotNull Collection<UUID> punishedPlayers) {
        this.punishedPlayers = punishedPlayers;
    }

    public Collection<UUID> getPunishedPlayers() {
        return punishedPlayers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
