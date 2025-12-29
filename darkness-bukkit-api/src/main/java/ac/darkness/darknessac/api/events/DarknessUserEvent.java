package ac.darkness.darknessac.api.events;

import ac.darkness.darknessac.api.DarknessUser;

@Deprecated(since = "1.2.1.0", forRemoval = true)
public interface DarknessUserEvent {

    DarknessUser getUser();

    default DarknessUser getPlayer() {
        return getUser();
    }

}
