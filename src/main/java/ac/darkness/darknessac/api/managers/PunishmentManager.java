package ac.darkness.darknessac.api.managers;

import ac.darkness.darknessac.api.DarknessIdentity;
import ac.darkness.darknessac.api.annotation.ThreadSafe;
import ac.darkness.darknessac.api.punishment.AnimationMode;
import ac.darkness.darknessac.api.punishment.PunishAnimation;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Dedicated manager for anticheat punishments, ban-waves, and punishment animation lifecycle.
 */
@ThreadSafe
public interface PunishmentManager {

    void punish(@NotNull UUID uuid, @NotNull String reason);

    default void punish(@NotNull Player player, @NotNull String reason) {
        punish(player.getUniqueId(), reason);
    }

    default void punish(@NotNull DarknessIdentity identity, @NotNull String reason) {
        punish(identity.getUniqueId(), reason);
    }

    void punish(@NotNull String playerName, @NotNull String reason);

    @NotNull
    default CompletableFuture<Void> punishAsync(@NotNull UUID uuid, @NotNull String reason) {
        return CompletableFuture.runAsync(() -> punish(uuid, reason));
    }

    @NotNull
    default CompletableFuture<Void> punishAsync(@NotNull Player player, @NotNull String reason) {
        return punishAsync(player.getUniqueId(), reason);
    }

    @NotNull
    default CompletableFuture<Void> punishAsync(@NotNull DarknessIdentity identity, @NotNull String reason) {
        return punishAsync(identity.getUniqueId(), reason);
    }

    boolean registerAnimation(@NotNull String name, @NotNull PunishAnimation animation);

    boolean registerAnimation(@NotNull String name, @NotNull AnimationMode animation);

    boolean unregisterAnimation(@NotNull String name);

    @Nullable
    PunishAnimation getAnimation(@NotNull String name);
}
