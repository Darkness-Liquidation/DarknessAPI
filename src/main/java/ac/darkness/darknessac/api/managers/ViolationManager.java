package ac.darkness.darknessac.api.managers;

import ac.darkness.darknessac.api.annotation.ThreadSafe;
import ac.darkness.darknessac.api.custom.CheckData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Dedicated manager for reading check telemetry, category violation metrics,
 * and performing safe violation resets without corrupting internal tracker buffers.
 */
@ThreadSafe
public interface ViolationManager {

    double getTotalViolations(@NotNull UUID uuid);

    default double getTotalViolations(@NotNull Player player) {
        return getTotalViolations(player.getUniqueId());
    }

    @NotNull
    default CompletableFuture<Double> getTotalViolationsAsync(@NotNull UUID uuid) {
        return CompletableFuture.completedFuture(getTotalViolations(uuid));
    }

    double getCombatViolations(@NotNull UUID uuid);

    default double getCombatViolations(@NotNull Player player) {
        return getCombatViolations(player.getUniqueId());
    }

    double getMovementViolations(@NotNull UUID uuid);

    default double getMovementViolations(@NotNull Player player) {
        return getMovementViolations(player.getUniqueId());
    }

    double getWorldViolations(@NotNull UUID uuid);

    default double getWorldViolations(@NotNull Player player) {
        return getWorldViolations(player.getUniqueId());
    }

    @NotNull
    Collection<CheckData> getCheckData(@NotNull UUID uuid);

    @NotNull
    default Collection<CheckData> getCheckData(@NotNull Player player) {
        return getCheckData(player.getUniqueId());
    }

    /**
     * Safely resets violations for the given check back to 0.0 without invalidating mathematical prediction buffers.
     *
     * @param uuid the player's UUID
     * @param check the check name (e.g. "KillAura")
     * @return true if found and reset
     */
    boolean resetViolations(@NotNull UUID uuid, @NotNull String check);

    default boolean resetViolations(@NotNull Player player, @NotNull String check) {
        return resetViolations(player.getUniqueId(), check);
    }

    /**
     * Safely resets violations for the specific check subtype back to 0.0.
     *
     * @param uuid the player's UUID
     * @param check the check name
     * @param type the subtype (e.g. "A")
     * @return true if found and reset
     */
    boolean resetViolations(@NotNull UUID uuid, @NotNull String check, @NotNull String type);

    default boolean resetViolations(@NotNull Player player, @NotNull String check, @NotNull String type) {
        return resetViolations(player.getUniqueId(), check, type);
    }

    /**
     * Simulates an anticheat check flag.
     *
     * @param uuid the player's UUID
     * @param check the check name
     * @param type the subtype
     * @param verbose verbose details
     * @return true if check existed and flag executed
     */
    boolean simulateFlag(@NotNull UUID uuid, @NotNull String check, @NotNull String type, @NotNull String verbose);

    default boolean simulateFlag(@NotNull Player player, @NotNull String check, @NotNull String type, @NotNull String verbose) {
        return simulateFlag(player.getUniqueId(), check, type, verbose);
    }
}
