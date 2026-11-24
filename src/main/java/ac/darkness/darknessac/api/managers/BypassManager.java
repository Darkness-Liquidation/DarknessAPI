package ac.darkness.darknessac.api.managers;

import ac.darkness.darknessac.api.CheckCategory;
import ac.darkness.darknessac.api.DarknessIdentity;
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

    default void setTempBypass(@NotNull DarknessIdentity identity, long seconds) {
        setTempBypass(identity.getUniqueId(), seconds);
    }

    void setTempBypassTicks(@NotNull UUID uuid, long ticks);

    default void setTempBypassTicks(@NotNull Player player, long ticks) {
        setTempBypassTicks(player.getUniqueId(), ticks);
    }

    default void setTempBypassTicks(@NotNull DarknessIdentity identity, long ticks) {
        setTempBypassTicks(identity.getUniqueId(), ticks);
    }

    void setTempBypass(@NotNull UUID uuid, @NotNull String checkName, long seconds);

    default void setTempBypass(@NotNull Player player, @NotNull String checkName, long seconds) {
        setTempBypass(player.getUniqueId(), checkName, seconds);
    }

    default void setTempBypass(@NotNull DarknessIdentity identity, @NotNull String checkName, long seconds) {
        setTempBypass(identity.getUniqueId(), checkName, seconds);
    }

    void setTempBypassTicks(@NotNull UUID uuid, @NotNull String checkName, long ticks);

    default void setTempBypassTicks(@NotNull Player player, @NotNull String checkName, long ticks) {
        setTempBypassTicks(player.getUniqueId(), checkName, ticks);
    }

    default void setTempBypassTicks(@NotNull DarknessIdentity identity, @NotNull String checkName, long ticks) {
        setTempBypassTicks(identity.getUniqueId(), checkName, ticks);
    }

    void setCategoryTempBypass(@NotNull UUID uuid, @NotNull CheckCategory category, long seconds);

    default void setCategoryTempBypass(@NotNull Player player, @NotNull CheckCategory category, long seconds) {
        setCategoryTempBypass(player.getUniqueId(), category, seconds);
    }

    default void setCategoryTempBypass(@NotNull DarknessIdentity identity, @NotNull CheckCategory category, long seconds) {
        setCategoryTempBypass(identity.getUniqueId(), category, seconds);
    }

    void setCategoryTempBypassTicks(@NotNull UUID uuid, @NotNull CheckCategory category, long ticks);

    default void setCategoryTempBypassTicks(@NotNull Player player, @NotNull CheckCategory category, long ticks) {
        setCategoryTempBypassTicks(player.getUniqueId(), category, ticks);
    }

    default void setCategoryTempBypassTicks(@NotNull DarknessIdentity identity, @NotNull CheckCategory category, long ticks) {
        setCategoryTempBypassTicks(identity.getUniqueId(), category, ticks);
    }

    boolean isBypassing(@NotNull UUID uuid);

    default boolean isBypassing(@NotNull Player player) {
        return isBypassing(player.getUniqueId());
    }

    default boolean isBypassing(@NotNull DarknessIdentity identity) {
        return isBypassing(identity.getUniqueId());
    }

    boolean isBypassing(@NotNull UUID uuid, @NotNull String checkName);

    default boolean isBypassing(@NotNull Player player, @NotNull String checkName) {
        return isBypassing(player.getUniqueId(), checkName);
    }

    default boolean isBypassing(@NotNull DarknessIdentity identity, @NotNull String checkName) {
        return isBypassing(identity.getUniqueId(), checkName);
    }

    boolean isBypassing(@NotNull UUID uuid, @NotNull CheckCategory category);

    default boolean isBypassing(@NotNull Player player, @NotNull CheckCategory category) {
        return isBypassing(player.getUniqueId(), category);
    }

    default boolean isBypassing(@NotNull DarknessIdentity identity, @NotNull CheckCategory category) {
        return isBypassing(identity.getUniqueId(), category);
    }

    void resetBypass(@NotNull UUID uuid);

    default void resetBypass(@NotNull Player player) {
        resetBypass(player.getUniqueId());
    }

    default void resetBypass(@NotNull DarknessIdentity identity) {
        resetBypass(identity.getUniqueId());
    }
}
