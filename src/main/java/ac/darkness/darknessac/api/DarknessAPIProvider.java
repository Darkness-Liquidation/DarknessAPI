package ac.darkness.darknessac.api;

import java.util.concurrent.CompletableFuture;

public final class DarknessAPIProvider {
    private static DarknessAbstractAPI instance;
    private static final CompletableFuture<DarknessAbstractAPI> futureInstance = new CompletableFuture<>();

    private DarknessAPIProvider() {}

    public static void init(DarknessAbstractAPI api) {
        instance = api;
        DarknessAPI.INSTANCE = api;
        if (!futureInstance.isDone()) {
            futureInstance.complete(api);
        }
    }

    public static boolean isInitialized() {
        return instance != null;
    }

    public static DarknessAbstractAPI get() {
        if (instance == null) {
            throw new IllegalStateException("DarknessAPI is not loaded. Ensure Darkness anticheat is enabled.");
        }
        return instance;
    }

    public static CompletableFuture<DarknessAbstractAPI> getAsync() {
        return futureInstance;
    }
}
