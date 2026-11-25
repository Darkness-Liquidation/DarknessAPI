package ac.darkness.darknessac.api.config;

import java.util.concurrent.CompletableFuture;

/**
 * Contract for components that reload from a {@link ConfigManager}.
 */
public interface ConfigReloadable {

    /**
     * Reloads configuration values from the given config manager.
     */
    void reload(ConfigManager config);

    /**
     * Asynchronously reloads configuration values.
     */
    default CompletableFuture<Boolean> reloadAsync(ConfigManager config) {
        return CompletableFuture.supplyAsync(() -> {
            reload(config);
            return true;
        });
    }
}

