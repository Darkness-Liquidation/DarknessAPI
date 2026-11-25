package ac.darkness.darknessac.api.common;

import java.util.concurrent.CompletableFuture;

/**
 * Common contract for components in Darkness that support reloading runtime state.
 */
public interface BasicReloadable {

    /**
     * Synchronously reloads the component.
     */
    void reload();

    /**
     * Indicates whether this component should preferably be reloaded asynchronously.
     */
    default boolean isLoadedAsync() {
        return false;
    }

    /**
     * Asynchronously reloads the component.
     */
    default CompletableFuture<Boolean> reloadAsync() {
        return CompletableFuture.supplyAsync(() -> {
            reload();
            return true;
        });
    }
}
