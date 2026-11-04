package ac.darkness.darknessac.api.punishment;

import ac.darkness.darknessac.api.DarknessUser;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a custom punishment animation that can be registered and used
 * when punishing players for cheating.
 */
@FunctionalInterface
public interface PunishAnimation {
    /**
     * Executes the punishment animation for the given player.
     *
     * @param user The user to punish
     * @param commands The list of commands to execute after the animation completes
     */
    void execute(@NotNull DarknessUser user, @NotNull List<String> commands);

    /**
     * Compatibility method for generic object references.
     *
     * @param user The user object (DarknessUser)
     * @param commands The list of commands to execute
     */
    default void execute(@NotNull Object user, @NotNull List<String> commands) {
        if (user instanceof DarknessUser) {
            execute((DarknessUser) user, commands);
        }
    }
}
