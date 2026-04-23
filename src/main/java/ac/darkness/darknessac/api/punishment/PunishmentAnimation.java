package ac.darkness.darknessac.api.punishment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a custom punishment animation that can be registered and used
 * when punishing players for cheating.
 */
@FunctionalInterface
public interface PunishmentAnimation {
    /**
     * Executes the punishment animation for the given player.
     *
     * @param user The DarknessUser to punish
     * @param commands The list of commands to execute after the animation completes
     */
    void execute(@NotNull Object user, @NotNull List<String> commands);
}
