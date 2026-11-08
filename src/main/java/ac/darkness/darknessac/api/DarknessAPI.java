package ac.darkness.darknessac.api;

import ac.darkness.darknessac.api.managers.BypassManager;
import ac.darkness.darknessac.api.managers.PunishmentManager;
import ac.darkness.darknessac.api.managers.UserManager;
import ac.darkness.darknessac.api.managers.ViolationManager;
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

    // ------------------- MANAGERS -------------------

    @NotNull
    public static UserManager getUserManager() {
        return get().getUserManager();
    }

    @NotNull
    public static BypassManager getBypassManager() {
        return get().getBypassManager();
    }

    @NotNull
    public static PunishmentManager getPunishmentManager() {
        return get().getPunishmentManager();
    }

    @NotNull
    public static ViolationManager getViolationManager() {
        return get().getViolationManager();
    }

    // ------------------- USER RESOLUTION -------------------

    @Nullable
    public static DarknessUser getUser(@NotNull Player player) {
        return getUser(player.getUniqueId());
    }

    @Nullable
    public static DarknessUser getUser(@NotNull UUID uuid) {
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

    // ------------------- CONVENIENCE SHORTCUTS -------------------

    public static void punish(@NotNull Player player, @NotNull String reason) {
        getPunishmentManager().punish(player, reason);
    }

    public static void punish(@NotNull UUID uuid, @NotNull String reason) {
        getPunishmentManager().punish(uuid, reason);
    }

    public static void setTempBypass(@NotNull Player player, long seconds) {
        getBypassManager().setTempBypass(player, seconds);
    }

    public static boolean isBypassing(@NotNull Player player) {
        return getBypassManager().isBypassing(player);
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
