package ac.darkness.darknessac.api;

import ac.darkness.darknessac.api.punishment.AnimationMode;
import ac.darkness.darknessac.api.punishment.PunishAnimation;
import ac.darkness.darknessac.api.punishment.PunishAnimationManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class DarknessAPI {

    public static volatile DarknessAbstractAPI INSTANCE;

    private DarknessAPI() {}

    public static boolean isInitialized() {
        return INSTANCE != null || DarknessAPIProvider.isInitialized();
    }

    @NotNull
    public static DarknessAbstractAPI get() {
        if (INSTANCE != null) return INSTANCE;
        return DarknessAPIProvider.get();
    }

    @NotNull
    public static DarknessAbstractAPI getInstance() {
        return get();
    }

    // ------------------- USER RESOLUTION -------------------

    @Nullable
    public static DarknessUser getUser(@NotNull Player player) {
        return player != null ? getUser(player.getUniqueId()) : null;
    }

    @Nullable
    public static DarknessUser getUser(@NotNull UUID uuid) {
        if (uuid == null) return null;
        DarknessAbstractAPI api = INSTANCE != null ? INSTANCE
                : (DarknessAPIProvider.isInitialized() ? DarknessAPIProvider.get() : null);
        return api != null ? api.getDarknessUser(uuid) : null;
    }

    @Nullable
    public static DarknessUser getUser(@NotNull Object playerOrUuid) {
        if (playerOrUuid instanceof DarknessUser u) return u;
        if (playerOrUuid instanceof Player p) return getUser(p.getUniqueId());
        if (playerOrUuid instanceof UUID id) return getUser(id);
        if (playerOrUuid instanceof DarknessIdentity id) return getUser(id.getUniqueId());
        return null;
    }

    // ------------------- ANIMATIONS -------------------

    public static boolean registerAnimation(@NotNull String name,
                                            @NotNull PunishAnimation animation) {
        return PunishAnimationManager.registerAnimation(name, animation);
    }

    public static boolean registerAnimation(@NotNull String name,
                                            @NotNull AnimationMode animation) {
        return PunishAnimationManager.registerAnimation(name, animation);
    }

    public static boolean unregisterAnimation(@NotNull String name) {
        return PunishAnimationManager.unregisterAnimation(name);
    }

    @Nullable
    public static PunishAnimation getAnimation(@NotNull String name) {
        return PunishAnimationManager.getAnimation(name);
    }
}
