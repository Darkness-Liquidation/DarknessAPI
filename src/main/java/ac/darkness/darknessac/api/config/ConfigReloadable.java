package ac.darkness.darknessac.api.config;

import java.util.concurrent.CompletableFuture;

public interface ConfigReloadable {

    void reload(ConfigManager config);

    default CompletableFuture<Boolean> reloadAsync(ConfigManager config) {
        return CompletableFuture.supplyAsync(() -> {
            reload(config);
            return true;
        });
    }
}
