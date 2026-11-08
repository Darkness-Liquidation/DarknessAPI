package ac.darkness.darknessac.api.common;

import java.util.concurrent.CompletableFuture;

public interface BasicReloadable {
    void reload();

    default boolean isLoadedAsync() {
        return false;
    }

    default CompletableFuture<Boolean> reloadAsync() {
        return CompletableFuture.supplyAsync(() -> {
            reload();
            return true;
        });
    }
}