package ac.darkness.darknessac.api.feature;

import ac.darkness.darknessac.api.common.BasicReloadable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Manages player-specific toggles and features in Darkness.
 */
public interface FeatureManager extends BasicReloadable {

    /**
     * Gets all registered feature identifiers.
     */
    @NotNull
    Collection<String> getFeatureKeys();

    /**
     * Retrieves the current state of a feature by its identifier.
     */
    @Nullable
    FeatureState getFeatureState(String key);

    /**
     * Checks whether the specified feature is currently enabled.
     */
    boolean isFeatureEnabled(String key);

    /**
     * Sets or overrides the state of a feature.
     */
    boolean setFeatureState(String key, FeatureState state);
}

