package ac.darkness.darknessac.api;

import ac.darkness.darknessac.api.punishment.AnimationMode;
import ac.darkness.darknessac.api.punishment.PunishAnimation;
import ac.darkness.darknessac.api.punishment.PunishAnimationManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Main API class for DarknessAC.
 * Provides convenient static methods for plugin integration.
 */
public class DarknessAPI {

    /**
     * Checks if DarknessAPI has been loaded and initialized.
     *
     * @return true if initialized and ready to use, false otherwise
     */
    public static boolean isInitialized() {
        return DarknessAPIProvider.isInitialized();
    }

    /**
     * Registers a custom punishment animation.
     *
     * @param name The name of the animation (case-insensitive)
     * @param animation The animation implementation
     * @return true if registered successfully, false if already exists
     */
    public static boolean registerAnimation(@NotNull String name, @NotNull PunishAnimation animation) {
        return PunishAnimationManager.registerAnimation(name, animation);
    }

    /**
     * Registers a custom punishment animation mode.
     * <p>
     * Darkness will handle ticking the animation and running punishment commands when it completes.
     *
     * @param name Animation name (case-insensitive, e.g. "FLAME", "LIGHTNING").
     * @param animation The animation mode to register.
     * @return True if registered, false if the name is already taken.
     */
    public static boolean registerAnimation(@NotNull String name, @NotNull AnimationMode animation) {
        return PunishAnimationManager.registerAnimation(name, animation);
    }

    /**
     * Unregisters a custom punishment animation.
     *
     * @param name The name of the animation
     * @return true if unregistered successfully, false if not found
     */
    public static boolean unregisterAnimation(@NotNull String name) {
        return PunishAnimationManager.unregisterAnimation(name);
    }

    /**
     * Gets a registered punishment animation by name.
     *
     * @param name The name of the animation
     * @return The animation, or null if not found
     */
    @Nullable
    public static PunishAnimation getAnimation(@NotNull String name) {
        return PunishAnimationManager.getAnimation(name);
    }

    /**
     * Gets a DarknessUser by Bukkit player.
     *
     * @param player The Bukkit player
     * @return DarknessUser or null if not found
     */
    @Nullable
    public static DarknessUser getUser(@NotNull Player player) {
        if (player == null || !isInitialized()) return null;
        return DarknessAPIProvider.get().getDarknessUser(player.getUniqueId());
    }

    /**
     * Gets a DarknessUser by player UUID.
     *
     * @param uuid The player UUID
     * @return DarknessUser or null if not found
     */
    @Nullable
    public static DarknessUser getUser(@NotNull UUID uuid) {
        if (uuid == null || !isInitialized()) return null;
        return DarknessAPIProvider.get().getDarknessUser(uuid);
    }

    /**
     * Gets the DarknessAbstractAPI instance.
     *
     * @return DarknessAbstractAPI instance
     */
    @NotNull
    public static DarknessAbstractAPI get() {
        return DarknessAPIProvider.get();
    }
}
