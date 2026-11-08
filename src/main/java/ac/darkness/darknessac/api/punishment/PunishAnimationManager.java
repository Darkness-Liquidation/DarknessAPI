package ac.darkness.darknessac.api.punishment;

import ac.darkness.darknessac.api.DarknessUser;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manager for registering and retrieving custom punishment animations.
 */
public class PunishAnimationManager {
    private static final Map<String, PunishAnimation> animations = new ConcurrentHashMap<>();

    /**
     * Internal animation executor used by {@link AnimationMode}.
     * Typically set by the Darkness core plugin on startup.
     */
    @Setter @Getter
    private static volatile AnimationExecutor executor;

    /**
     * Registers a custom punishment animation or animation constructor mode.
     *
     * @param name The name/identifier for the animation (case-insensitive)
     * @param animation The animation implementation
     * @return true if registered successfully, false if name already exists
     */
    public static boolean registerAnimation(@NotNull String name, @NotNull PunishAnimation animation) {
        String key = name.toUpperCase();
        if (animations.containsKey(key)) {
            return false;
        }
        animations.put(key, animation);
        return true;
    }

    /**
     * Unregisters a custom punishment animation.
     *
     * @param name The name of the animation to unregister
     * @return true if unregistered successfully, false if not found
     */
    public static boolean unregisterAnimation(@NotNull String name) {
        return animations.remove(name.toUpperCase()) != null;
    }

    /**
     * Retrieves a registered animation by name.
     *
     * @param name The name of the animation
     * @return The animation, or null if not found
     */
    @Nullable
    public static PunishAnimation getAnimation(@NotNull String name) {
        return animations.get(name.toUpperCase());
    }

    /**
     * Checks if an animation is registered.
     *
     * @param name The name of the animation
     * @return true if registered, false otherwise
     */
    public static boolean hasAnimation(@NotNull String name) {
        return animations.containsKey(name.toUpperCase());
    }

    /**
     * Clears all registered animations.
     */
    public static void clearAnimations() {
        animations.clear();
    }

    /**
     * Internal executor interface bridging {@link AnimationMode} to the Darkness execution engine.
     */
    @FunctionalInterface
    public interface AnimationExecutor {
        void execute(@NotNull AnimationMode mode, @NotNull DarknessUser user, @NotNull List<String> commands);
    }
}
