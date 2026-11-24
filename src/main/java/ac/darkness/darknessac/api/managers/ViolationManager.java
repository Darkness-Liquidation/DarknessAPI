package ac.darkness.darknessac.api.managers;

import ac.darkness.darknessac.api.CheckCategory;
import ac.darkness.darknessac.api.DarknessIdentity;
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

    default double getTotalViolations(@NotNull DarknessIdentity identity) {
        return getTotalViolations(identity.getUniqueId());
    }

    @NotNull
    default CompletableFuture<Double> getTotalViolationsAsync(@NotNull UUID uuid) {
        return CompletableFuture.completedFuture(getTotalViolations(uuid));
    }

    @NotNull
    default CompletableFuture<Double> getTotalViolationsAsync(@NotNull Player player) {
        return CompletableFuture.completedFuture(getTotalViolations(player.getUniqueId()));
    }

    @NotNull
    default CompletableFuture<Double> getTotalViolationsAsync(@NotNull DarknessIdentity identity) {
        return CompletableFuture.completedFuture(getTotalViolations(identity.getUniqueId()));
    }

    double getCombatViolations(@NotNull UUID uuid);

    default double getCombatViolations(@NotNull Player player) {
        return getCombatViolations(player.getUniqueId());
    }

    default double getCombatViolations(@NotNull DarknessIdentity identity) {
        return getCombatViolations(identity.getUniqueId());
    }

    double getMovementViolations(@NotNull UUID uuid);

    default double getMovementViolations(@NotNull Player player) {
        return getMovementViolations(player.getUniqueId());
    }

    default double getMovementViolations(@NotNull DarknessIdentity identity) {
        return getMovementViolations(identity.getUniqueId());
    }

    double getWorldViolations(@NotNull UUID uuid);

    default double getWorldViolations(@NotNull Player player) {
        return getWorldViolations(player.getUniqueId());
    }

    default double getWorldViolations(@NotNull DarknessIdentity identity) {
        return getWorldViolations(identity.getUniqueId());
    }

    default double getCategoryViolations(@NotNull UUID uuid, @NotNull CheckCategory category) {
        return switch (category) {
            case COMBAT -> getCombatViolations(uuid);
            case MOVEMENT -> getMovementViolations(uuid);
            case WORLD -> getWorldViolations(uuid);
            default -> 0.0;
        };
    }

    default double getCategoryViolations(@NotNull Player player, @NotNull CheckCategory category) {
        return getCategoryViolations(player.getUniqueId(), category);
    }

    default double getCategoryViolations(@NotNull DarknessIdentity identity, @NotNull CheckCategory category) {
        return getCategoryViolations(identity.getUniqueId(), category);
    }

    @NotNull
    Collection<CheckData> getCheckData(@NotNull UUID uuid);

    @NotNull
    default Collection<CheckData> getCheckData(@NotNull Player player) {
        return getCheckData(player.getUniqueId());
    }

    @NotNull
    default Collection<CheckData> getCheckData(@NotNull DarknessIdentity identity) {
        return getCheckData(identity.getUniqueId());
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

    default boolean resetViolations(@NotNull DarknessIdentity identity, @NotNull String check) {
        return resetViolations(identity.getUniqueId(), check);
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

    default boolean resetViolations(@NotNull DarknessIdentity identity, @NotNull String check, @NotNull String type) {
        return resetViolations(identity.getUniqueId(), check, type);
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

    default boolean simulateFlag(@NotNull DarknessIdentity identity, @NotNull String check, @NotNull String type, @NotNull String verbose) {
        return simulateFlag(identity.getUniqueId(), check, type, verbose);
    }
}
