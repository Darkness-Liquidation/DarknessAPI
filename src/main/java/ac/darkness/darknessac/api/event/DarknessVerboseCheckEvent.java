package ac.darkness.darknessac.api.event;

import ac.darkness.darknessac.api.AbstractCheck;
import ac.darkness.darknessac.api.DarknessUser;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DarknessVerboseCheckEvent extends DarknessCheckEvent {
    @Getter
    @NotNull
    private final String verbose;

    public DarknessVerboseCheckEvent(@NotNull DarknessUser user, @Nullable AbstractCheck check, @NotNull String verbose) {
        super(user, check, true);
        this.verbose = verbose;
    }

    public DarknessVerboseCheckEvent(@NotNull DarknessUser user, @Nullable AbstractCheck check, @NotNull String verbose, boolean async) {
        super(user, check, async);
        this.verbose = verbose;
    }
}
