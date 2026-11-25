package ac.darkness.darknessac.api.event;

import ac.darkness.darknessac.api.AbstractCheck;
import ac.darkness.darknessac.api.DarknessUser;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DarknessCheckEvent extends DarknessEvent implements DarknessUserEvent {
    @Getter
    @NotNull
    private final DarknessUser user;

    @Getter
    @Nullable
    private final AbstractCheck checkInstance;

    public DarknessCheckEvent(@NotNull DarknessUser user, @Nullable AbstractCheck checkInstance) {
        super(true);
        this.user = user;
        this.checkInstance = checkInstance;
    }

    public DarknessCheckEvent(@NotNull DarknessUser user, @Nullable AbstractCheck checkInstance, boolean async) {
        super(async);
        this.user = user;
        this.checkInstance = checkInstance;
    }

    @Override
    public boolean isCancellable() {
        return true;
    }
}
