package ac.darkness.darknessac.api.managers;

import ac.darkness.darknessac.api.DarknessIdentity;
import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.annotation.ThreadSafe;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Dedicated manager for player session resolution and lifecycle.
 * All queries are thread-safe and safe to execute from async tasks or arbitrary Folia threads.
 */
@ThreadSafe
public interface UserManager {

    /**
     * Resolves an online player's anticheat session by unique ID.
     *
     * @param uuid the player's UUID
     * @return active {@link DarknessUser} or {@code null} if offline
     */
    @Nullable
    DarknessUser getUser(@NotNull UUID uuid);

    /**
     * Resolves an online player's anticheat session.
     *
     * @param player the Bukkit player
     * @return active {@link DarknessUser} or {@code null} if offline
     */
    @Nullable
    default DarknessUser getUser(@NotNull Player player) {
        return getUser(player.getUniqueId());
    }

    /**
     * Resolves an online player's anticheat session by identity.
     *
     * @param identity the player's identity
     * @return active {@link DarknessUser} or {@code null} if offline
     */
    @Nullable
    default DarknessUser getUser(@NotNull DarknessIdentity identity) {
        return getUser(identity.getUniqueId());
    }

    /**
     * Looks up an online player's anticheat session by unique ID wrapped in an {@link Optional}.
     *
     * @param uuid the player's UUID
     * @return optional containing the active {@link DarknessUser} if online
     */
    @NotNull
    default Optional<DarknessUser> findUser(@NotNull UUID uuid) {
        return Optional.ofNullable(getUser(uuid));
    }

    /**
     * Looks up an online player's anticheat session wrapped in an {@link Optional}.
     *
     * @param player the Bukkit player
     * @return optional containing the active {@link DarknessUser} if online
     */
    @NotNull
    default Optional<DarknessUser> findUser(@NotNull Player player) {
        return findUser(player.getUniqueId());
    }

    /**
     * Looks up an online player's anticheat session by identity wrapped in an {@link Optional}.
     *
     * @param identity the player's identity
     * @return optional containing the active {@link DarknessUser} if online
     */
    @NotNull
    default Optional<DarknessUser> findUser(@NotNull DarknessIdentity identity) {
        return findUser(identity.getUniqueId());
    }

    /**
     * Asynchronously resolves an online player's anticheat session.
     *
     * @param uuid the player's UUID
     * @return future completed with {@link DarknessUser} or {@code null}
     */
    @NotNull
    default CompletableFuture<DarknessUser> getUserAsync(@NotNull UUID uuid) {
        return CompletableFuture.completedFuture(getUser(uuid));
    }

    /**
     * Asynchronously resolves an online player's anticheat session.
     *
     * @param player the Bukkit player
     * @return future completed with {@link DarknessUser} or {@code null}
     */
    @NotNull
    default CompletableFuture<DarknessUser> getUserAsync(@NotNull Player player) {
        return getUserAsync(player.getUniqueId());
    }

    /**
     * Asynchronously resolves an online player's anticheat session by identity.
     *
     * @param identity the player's identity
     * @return future completed with {@link DarknessUser} or {@code null}
     */
    @NotNull
    default CompletableFuture<DarknessUser> getUserAsync(@NotNull DarknessIdentity identity) {
        return getUserAsync(identity.getUniqueId());
    }

    /**
     * Returns all currently active online anticheat player sessions.
     *
     * @return collection of active player sessions
     */
    @NotNull
    Collection<? extends DarknessUser> getOnlineUsers();
}
