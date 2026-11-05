package ac.darkness.darknessac.api;

import ac.darkness.darknessac.api.common.BasicReloadable;
import ac.darkness.darknessac.api.config.ConfigReloadable;
import ac.darkness.darknessac.api.feature.FeatureManager;
import ac.darkness.darknessac.api.handler.UserHandlerHolder;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.UUID;

public interface DarknessUser extends ConfigReloadable, BasicReloadable, UserHandlerHolder, DarknessIdentity {

    String getName();

    String getBrand();

    @Nullable String getWorldName();

    @Nullable UUID getWorldUID();

    int getTransactionPing();

    int getKeepAlivePing();

    String getVersionName();

    /**
     * @return Client network protocol version (e.g. 763 for 1.20, 47 for 1.8), or 0 if unknown.
     */
    default int getProtocolVersion() {
        return 0;
    }

    InetSocketAddress getAddress();

    double getHorizontalSensitivity();

    double getVerticalSensitivity();

    /**
     * Returns horizontal sensitivity converted to human-readable percentage (0 to 200%),
     * matching the in-game Minecraft settings slider.
     *
     * @return sensitivity percentage (0 - 200%)
     */
    default int getHumanSensitivityX() {
        return (int) Math.round(getHorizontalSensitivity() * 200.0);
    }

    /**
     * Returns vertical sensitivity converted to human-readable percentage (0 to 200%),
     * matching the in-game Minecraft settings slider.
     *
     * @return sensitivity percentage (0 - 200%)
     */
    default int getHumanSensitivityY() {
        return (int) Math.round(getVerticalSensitivity() * 200.0);
    }

    /**
     * @return The player's overall trust level (GREEN = clean, ORANGE = suspicious, RED = flagged/punished).
     */
    default TrustLevel getTrustLevel() {
        return TrustLevel.GREEN;
    }

    /**
     * @return Trust score from 0 (completely untrusted) to 100 (fully clean).
     */
    default int getTrustScore() {
        return 100;
    }

    default double getX() {
        return 0.0;
    }

    default double getY() {
        return 0.0;
    }

    default double getZ() {
        return 0.0;
    }

    default float getYaw() {
        return 0.0f;
    }

    default float getPitch() {
        return 0.0f;
    }

    boolean isVanillaMath();

    void updatePermissions();

    Collection<? extends AbstractCheck> getChecks();

    /**
     * Finds a check for this user by check name or alternative name.
     *
     * @param name Check name (e.g. "FlyA", "AimA", "KillAura")
     * @return The check instance or null if not found
     */
    @Nullable
    default AbstractCheck getCheck(String name) {
        if (name == null) return null;
        for (AbstractCheck check : getChecks()) {
            if (name.equalsIgnoreCase(check.getCheckName()) || name.equalsIgnoreCase(check.getAlternativeName())) {
                return check;
            }
        }
        return null;
    }

    /**
     * Runs the runnable on the player's netty thread. This may need to be used
     * to access parts of the API safely. This might be removed in future for
     * simplicity.
     *
     * @param runnable
     */
    void runSafely(Runnable runnable);

    /**
     * Retrieves the last transaction received by the player.
     */
    int getLastTransactionReceived();

    /**
     * Retrieves the last transaction sent by the player.
     */
    int getLastTransactionSent();

    /**
     * Schedules a task to run based on the player's transaction.
     * This needs to be executed on the player's netty thread. You can use {@link DarknessUser#runSafely(Runnable)} to ensure this.
     *
     * @param transaction If the player's transaction is greater than or equal to this, the task will run
     * @param runnable    The task that should run
     */
    void addRealTimeTask(int transaction, Runnable runnable);

    default void addRealTimeTaskNow(Runnable runnable) {
        addRealTimeTask(getLastTransactionSent(), runnable);
    }

    default void addRealTimeTaskNext(Runnable runnable) {
        addRealTimeTask(getLastTransactionSent() + 1, runnable);
    }

    /**
     * An easier way to manage per player features that's persistent between reloads.
     */
    FeatureManager getFeatureManager();

    /**
     * Sends a message to the player.
     * @param message Message to send
     */
    void sendMessage(String message);

    boolean hasPermission(String permission);
}
