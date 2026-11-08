package ac.darkness.darknessac.api.managers;

import ac.darkness.darknessac.api.annotation.ThreadSafe;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Dedicated manager for global, category-specific, and check-specific temporary bypasses.
 * All duration methods safely recalculate expiration without unbounded stacking.
 */
@ThreadSafe
public interface BypassManager {

    void setTempBypass(@NotNull UUID uuid, long seconds);

    default void setTempBypass(@NotNull Player player, long seconds) {
        setTempBypass(player.getUniqueId(), seconds);
    }

    void setTempBypassTicks(@NotNull UUID uuid, long ticks);

    default void setTempBypassTicks(@NotNull Player player, long ticks) {
        setTempBypassTicks(player.getUniqueId(), ticks);
    }

    void setTempBypass(@NotNull UUID uuid, @NotNull String checkName, long seconds);

    default void setTempBypass(@NotNull Player player, @NotNull String checkName, long seconds) {
        setTempBypass(player.getUniqueId(), checkName, seconds);
    }

    void setTempBypassTicks(@NotNull UUID uuid, @NotNull String checkName, long ticks);

    default void setTempBypassTicks(@NotNull Player player, @NotNull String checkName, long ticks) {
        setTempBypassTicks(player.getUniqueId(), checkName, ticks);
    }

    void setCategoryTempBypass(@NotNull UUID uuid, @NotNull String category, long seconds);

    default void setCategoryTempBypass(@NotNull Player player, @NotNull String category, long seconds) {
        setCategoryTempBypass(player.getUniqueId(), category, seconds);
    }

    void setCategoryTempBypassTicks(@NotNull UUID uuid, @NotNull String category, long ticks);

    default void setCategoryTempBypassTicks(@NotNull Player player, @NotNull String category, long ticks) {
        setCategoryTempBypassTicks(player.getUniqueId(), category, ticks);
    }

    boolean isBypassing(@NotNull UUID uuid);

    default boolean isBypassing(@NotNull Player player) {
        return isBypassing(player.getUniqueId());
    }

    boolean isBypassing(@NotNull UUID uuid, @NotNull String checkName);

    default boolean isBypassing(@NotNull Player player, @NotNull String checkName) {
        return isBypassing(player.getUniqueId(), checkName);
    }

    void resetBypass(@NotNull UUID uuid);

    default void resetBypass(@NotNull Player player) {
        resetBypass(player.getUniqueId());
    }
}
