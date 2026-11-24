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

import java.util.Optional;
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

    @NotNull
    public static Optional<DarknessUser> findUser(@NotNull Player player) {
        return Optional.ofNullable(getUser(player));
    }

    @NotNull
    public static Optional<DarknessUser> findUser(@NotNull UUID uuid) {
        return Optional.ofNullable(getUser(uuid));
    }

    @NotNull
    public static Optional<DarknessUser> findUser(@NotNull Object playerOrUuid) {
        return Optional.ofNullable(getUser(playerOrUuid));
    }

    // ------------------- CONVENIENCE SHORTCUTS -------------------

    public static void punish(@NotNull Player player, @NotNull String reason) {
        getPunishmentManager().punish(player, reason);
    }

    public static void punish(@NotNull UUID uuid, @NotNull String reason) {
        getPunishmentManager().punish(uuid, reason);
    }

    public static void punish(@NotNull DarknessIdentity identity, @NotNull String reason) {
        getPunishmentManager().punish(identity, reason);
    }

    public static void setTempBypass(@NotNull Player player, long seconds) {
        getBypassManager().setTempBypass(player, seconds);
    }

    public static void setTempBypass(@NotNull UUID uuid, long seconds) {
        getBypassManager().setTempBypass(uuid, seconds);
    }

    public static void setTempBypass(@NotNull DarknessIdentity identity, long seconds) {
        getBypassManager().setTempBypass(identity, seconds);
    }

    public static void setTempBypassTicks(@NotNull Player player, long ticks) {
        getBypassManager().setTempBypassTicks(player, ticks);
    }

    public static void setTempBypassTicks(@NotNull UUID uuid, long ticks) {
        getBypassManager().setTempBypassTicks(uuid, ticks);
    }

    public static void setTempBypassTicks(@NotNull DarknessIdentity identity, long ticks) {
        getBypassManager().setTempBypassTicks(identity, ticks);
    }

    public static void setCategoryTempBypass(@NotNull Player player, @NotNull CheckCategory category, long seconds) {
        getBypassManager().setCategoryTempBypass(player, category, seconds);
    }

    public static void setCategoryTempBypass(@NotNull UUID uuid, @NotNull CheckCategory category, long seconds) {
        getBypassManager().setCategoryTempBypass(uuid, category, seconds);
    }

    public static void setCategoryTempBypassTicks(@NotNull Player player, @NotNull CheckCategory category, long ticks) {
        getBypassManager().setCategoryTempBypassTicks(player, category, ticks);
    }

    public static void setCategoryTempBypassTicks(@NotNull UUID uuid, @NotNull CheckCategory category, long ticks) {
        getBypassManager().setCategoryTempBypassTicks(uuid, category, ticks);
    }

    public static boolean isBypassing(@NotNull Player player) {
        return getBypassManager().isBypassing(player);
    }

    public static boolean isBypassing(@NotNull UUID uuid) {
        return getBypassManager().isBypassing(uuid);
    }

    public static boolean isBypassing(@NotNull DarknessIdentity identity) {
        return getBypassManager().isBypassing(identity);
    }

    public static void setTempBypass(@NotNull Player player, @NotNull CheckType checkType, long seconds) {
        getBypassManager().setTempBypass(player, checkType, seconds);
    }

    public static void setTempBypass(@NotNull UUID uuid, @NotNull CheckType checkType, long seconds) {
        getBypassManager().setTempBypass(uuid, checkType, seconds);
    }

    public static void setTempBypass(@NotNull DarknessIdentity identity, @NotNull CheckType checkType, long seconds) {
        getBypassManager().setTempBypass(identity, checkType, seconds);
    }

    public static void setTempBypassTicks(@NotNull Player player, @NotNull CheckType checkType, long ticks) {
        getBypassManager().setTempBypassTicks(player, checkType, ticks);
    }

    public static void setTempBypassTicks(@NotNull UUID uuid, @NotNull CheckType checkType, long ticks) {
        getBypassManager().setTempBypassTicks(uuid, checkType, ticks);
    }

    public static void setTempBypassTicks(@NotNull DarknessIdentity identity, @NotNull CheckType checkType, long ticks) {
        getBypassManager().setTempBypassTicks(identity, checkType, ticks);
    }

    public static boolean isBypassing(@NotNull Player player, @NotNull CheckType checkType) {
        return getBypassManager().isBypassing(player, checkType);
    }

    public static boolean isBypassing(@NotNull UUID uuid, @NotNull CheckType checkType) {
        return getBypassManager().isBypassing(uuid, checkType);
    }

    public static boolean isBypassing(@NotNull DarknessIdentity identity, @NotNull CheckType checkType) {
        return getBypassManager().isBypassing(identity, checkType);
    }

    public static boolean isBypassing(@NotNull Player player, @NotNull CheckCategory category) {
        return getBypassManager().isBypassing(player, category);
    }

    public static boolean isBypassing(@NotNull UUID uuid, @NotNull CheckCategory category) {
        return getBypassManager().isBypassing(uuid, category);
    }

    public static double getTotalViolations(@NotNull Player player) {
        return getViolationManager().getTotalViolations(player);
    }

    public static double getTotalViolations(@NotNull UUID uuid) {
        return getViolationManager().getTotalViolations(uuid);
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
