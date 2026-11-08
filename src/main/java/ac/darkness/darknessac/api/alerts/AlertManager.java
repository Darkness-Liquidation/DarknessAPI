package ac.darkness.darknessac.api.alerts;

import ac.darkness.darknessac.api.DarknessUser;
import lombok.NonNull;

public interface AlertManager {

    boolean hasAlertsEnabled(@NonNull DarknessUser player);

    default boolean toggleAlerts(@NonNull DarknessUser player) {
        return toggleAlerts(player, false);
    }

    default boolean toggleAlerts(@NonNull DarknessUser player, boolean silent) {
        boolean newState = !hasAlertsEnabled(player);
        setAlertsEnabled(player, newState, silent);
        return newState;
    }

    default void setAlertsEnabled(@NonNull DarknessUser player, boolean enabled) {
        setAlertsEnabled(player, enabled, false);
    }

    void setAlertsEnabled(@NonNull DarknessUser player, boolean enabled, boolean silent);

    boolean hasVerboseEnabled(@NonNull DarknessUser player);

    default boolean toggleVerbose(@NonNull DarknessUser player) {
        return toggleVerbose(player, false);
    }

    default boolean toggleVerbose(@NonNull DarknessUser player, boolean silent) {
        boolean newState = !hasVerboseEnabled(player);
        setVerboseEnabled(player, newState, silent);
        return newState;
    }

    default void setVerboseEnabled(@NonNull DarknessUser player, boolean enabled) {
        setVerboseEnabled(player, enabled, false);
    }

    void setVerboseEnabled(@NonNull DarknessUser player, boolean enabled, boolean silent);

    boolean hasBrandsEnabled(@NonNull DarknessUser player);

    default boolean toggleBrands(@NonNull DarknessUser player) {
        return toggleBrands(player, false);
    }

    default boolean toggleBrands(@NonNull DarknessUser player, boolean silent) {
        boolean newState = !hasBrandsEnabled(player);
        setBrandsEnabled(player, newState, silent);
        return newState;
    }

    default void setBrandsEnabled(@NonNull DarknessUser player, boolean enabled) {
        setBrandsEnabled(player, enabled, false);
    }

    void setBrandsEnabled(@NonNull DarknessUser player, boolean enabled, boolean silent);

    void sendAlert(@NonNull String message);
}