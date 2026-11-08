package ac.darkness.darknessac.api.managers;

import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.annotation.ThreadSafe;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
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
     * Returns all currently active online anticheat player sessions.
     *
     * @return collection of active player sessions
     */
    @NotNull
    Collection<? extends DarknessUser> getOnlineUsers();
}
