package ac.darkness.darknessac.api;

import ac.darkness.darknessac.api.alerts.AlertManager;
import ac.darkness.darknessac.api.common.BasicReloadable;
import ac.darkness.darknessac.api.config.ConfigManager;
import ac.darkness.darknessac.api.config.ConfigReloadable;
import ac.darkness.darknessac.api.event.EventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface DarknessAbstractAPI extends ConfigReloadable, BasicReloadable {
    /**
     * Returns EventBus instanced used to register events and listen to Darkness events
     * @return {@link EventBus}
     */
    @NotNull EventBus getEventBus();

    /**
     * Retrieves a DarknessUser reference from the player's UUID.
     * @param uuid UUID of the player
     * @return DarknessUser
     */
    @Nullable DarknessUser getDarknessUser(UUID uuid);

    /**
     * Used to create or replace variables, such as %player%. This only works
     * for player related messages.
     * @param variable
     * @param replacement
     */
    void registerVariable(String variable, @Nullable Function<DarknessUser, String> replacement);

    /**
     * Used to create or replace static variables, such as %server%.
     * @param variable
     * @param replacement
     */
    void registerVariable(String variable, @Nullable String replacement);

    /**
     * Retrieves the plugin version of Darkness.
     * @return Darkness version
     */
    String getDarknessVersion();

    /**
     * Used for future expansion. Don't use this unless you know what you're doing.
     */
    void registerFunction(String key, @Nullable Function<Object, Object> function);

    /**
     * Used for future expansion. Don't use this unless you know what you're doing.
     */
    @Nullable Function<Object, Object> getFunction(String key);

    /**
     * Retrieves the alert manager.
     * @return AlertManager
     */
    AlertManager getAlertManager();

    /**
     * Retrieves the config manager.
     * @return Configurable
     */
    ConfigManager getConfigManager();

    /**
     * Reloads Darkness using the config file.
     */
    @Override
    default void reload() {
        reload(getConfigManager());
    }

    /**
     * Reloads Darkness asynchronously using the config file.
     * @return CompletableFuture
     */
    default CompletableFuture<Boolean> reloadAsync() {
        return reloadAsync(getConfigManager());
    }

    /**
     * Checks if the API has reached the start phase of the plugin.
     * @return boolean
     */
    boolean hasStarted();

    /**
     * Retrieves the current tick of the server.
     * @return int
     */
    int getCurrentTick();

}
