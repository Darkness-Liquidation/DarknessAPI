package ac.darkness.darknessac.api.event;

import ac.darkness.darknessac.api.DarknessUser;
import org.jetbrains.annotations.NotNull;

public interface DarknessUserEvent {
    @NotNull
    DarknessUser getUser();
}
