package ac.darkness.darknessac.api;

import java.util.concurrent.CompletableFuture;

public final class DarknessAPIProvider {
    private static DarknessAbstractAPI instance;
    private static final CompletableFuture<DarknessAbstractAPI> futureInstance = new CompletableFuture<>();

    private DarknessAPIProvider() {
        // Private constructor to prevent instantiation
    }

    /**
     * Initializes the DarknessAPI instance during mod loading.
     * This method should only be called once by the mod initializer.
     *
     * @param api The DarknessAbstractAPI instance to initialize.
     * @throws IllegalStateException If the API is already initialized.
     */
    public static void init(DarknessAbstractAPI api) {
        if (instance != null || futureInstance.isDone()) {
            throw new IllegalStateException("DarknessAPI is already initialized");
        }
        instance = api;
        futureInstance.complete(api); // Complete the future with the API instance
    }

    /**
     * Checks if the DarknessAPI has been initialized.
     *
     * @return true if initialized, false otherwise
     */
    public static boolean isInitialized() {
        return instance != null;
    }

    /**
     * Gets the DarknessAPI instance synchronously.
     *
     * @return The DarknessAbstractAPI instance.
     * @throws IllegalStateException If the API is not loaded.
     */
    public static DarknessAbstractAPI get() {
        if (instance == null) {
            throw new IllegalStateException("DarknessAPI is not loaded. Ensure the Darkness mod is installed and initialized.");
        }
        return instance;
    }

    /**
     * Gets the DarknessAPI instance asynchronously.
     * The returned CompletableFuture will complete when the DarknessAPI instance is available.
     * If the API is already loaded, the future will complete immediately.
     * If the API fails to load (e.g., the mod is not installed), the future will complete exceptionally.
     *
     * @return A CompletableFuture that completes with the DarknessAbstractAPI instance.
     */
    public static CompletableFuture<DarknessAbstractAPI> getAsync() {
        if (instance != null) {
            // If the instance is already loaded, return a completed future
            return CompletableFuture.completedFuture(instance);
        }
        return futureInstance;
    }
}
