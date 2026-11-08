package ac.darkness.darknessac.api;

import ac.darkness.darknessac.api.alerts.AlertManager;
import ac.darkness.darknessac.api.common.BasicReloadable;
import ac.darkness.darknessac.api.config.ConfigManager;
import ac.darkness.darknessac.api.config.ConfigReloadable;
import ac.darkness.darknessac.api.event.EventBus;
import ac.darkness.darknessac.api.managers.BypassManager;
import ac.darkness.darknessac.api.managers.PunishmentManager;
import ac.darkness.darknessac.api.managers.UserManager;
import ac.darkness.darknessac.api.managers.ViolationManager;
import ac.darkness.darknessac.api.punishment.AnimationMode;
import ac.darkness.darknessac.api.punishment.PunishAnimation;
import ac.darkness.darknessac.api.custom.CheckData;
import java.util.Collection;
import java.util.Collections;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@ac.darkness.darknessac.api.annotation.ThreadSafe
public interface DarknessAbstractAPI extends ConfigReloadable, BasicReloadable {

    @NotNull EventBus getEventBus();

    @Nullable DarknessUser getDarknessUser(UUID uuid);

    default @Nullable DarknessUser getDarknessUser(Player player) {
        return player != null ? getDarknessUser(player.getUniqueId()) : null;
    }

    default @Nullable DarknessUser getUser(Player player) {
        return getDarknessUser(player);
    }

    default @Nullable DarknessUser getUser(UUID uuid) {
        return getDarknessUser(uuid);
    }

    default @Nullable DarknessUser getUser(Object playerOrUuid) {
        if (playerOrUuid instanceof DarknessUser u) return u;
        if (playerOrUuid instanceof Player p) return getUser(p.getUniqueId());
        if (playerOrUuid instanceof UUID id) return getUser(id);
        if (playerOrUuid instanceof DarknessIdentity id) return getUser(id.getUniqueId());
        return null;
    }

    void registerVariable(String variable, @Nullable Function<DarknessUser, String> replacement);

    void registerVariable(String variable, @Nullable String replacement);

    String getDarknessVersion();

    void registerFunction(String key, @Nullable Function<Object, Object> function);

    @Nullable Function<Object, Object> getFunction(String key);

    AlertManager getAlertManager();

    ConfigManager getConfigManager();

    @Override
    default void reload() {
        reload(getConfigManager());
    }

    default CompletableFuture<Boolean> reloadAsync() {
        return reloadAsync(getConfigManager());
    }

    boolean hasStarted();

    int getCurrentTick();

    default void punish(Player player, String reason) {
        if (player != null) punish(player.getUniqueId(), reason);
    }

    default void punish(UUID uuid, String reason) {}

    default void punish(String playerName, String reason) {}

    default void setTempBypass(Player player, long seconds) {
        DarknessUser user = getUser(player);
        if (user != null) user.setTempBypass(seconds);
    }

    default void setTempBypassTicks(Player player, long ticks) {
        DarknessUser user = getUser(player);
        if (user != null) user.setTempBypassTicks(ticks);
    }

    default void setTempBypass(Player player, String checkName, long seconds) {
        DarknessUser user = getUser(player);
        if (user != null) user.setTempBypass(checkName, seconds);
    }

    default void setTempBypassTicks(Player player, String checkName, long ticks) {
        DarknessUser user = getUser(player);
        if (user != null) user.setTempBypassTicks(checkName, ticks);
    }

    default void setCategoryTempBypass(Player player, String category, long seconds) {
        DarknessUser user = getUser(player);
        if (user != null) user.setCategoryTempBypass(category, seconds);
    }

    default void setCategoryTempBypassTicks(Player player, String category, long ticks) {
        DarknessUser user = getUser(player);
        if (user != null) user.setCategoryTempBypassTicks(category, ticks);
    }

    default boolean isBypassing(Player player) {
        DarknessUser user = getUser(player);
        return user != null && user.isBypassing();
    }

    default boolean isBypassing(Player player, String checkName) {
        DarknessUser user = getUser(player);
        return user != null && user.isBypassing(checkName);
    }

    @NotNull
    default UserManager getUserManager() {
        return new UserManager() {
            @Override
            public DarknessUser getUser(@NotNull UUID uuid) {
                return DarknessAbstractAPI.this.getUser(uuid);
            }

            @NotNull
            @Override
            public Collection<? extends DarknessUser> getOnlineUsers() {
                return Collections.emptyList();
            }
        };
    }

    @NotNull
    default BypassManager getBypassManager() {
        return new BypassManager() {
            @Override
            public void setTempBypass(@NotNull UUID uuid, long seconds) {
                DarknessUser user = getUser(uuid);
                if (user != null) user.setTempBypass(seconds);
            }

            @Override
            public void setTempBypassTicks(@NotNull UUID uuid, long ticks) {
                DarknessUser user = getUser(uuid);
                if (user != null) user.setTempBypassTicks(ticks);
            }

            @Override
            public void setTempBypass(@NotNull UUID uuid, @NotNull String checkName, long seconds) {
                DarknessUser user = getUser(uuid);
                if (user != null) user.setTempBypass(checkName, seconds);
            }

            @Override
            public void setTempBypassTicks(@NotNull UUID uuid, @NotNull String checkName, long ticks) {
                DarknessUser user = getUser(uuid);
                if (user != null) user.setTempBypassTicks(checkName, ticks);
            }

            @Override
            public void setCategoryTempBypass(@NotNull UUID uuid, @NotNull String category, long seconds) {
                DarknessUser user = getUser(uuid);
                if (user != null) user.setCategoryTempBypass(category, seconds);
            }

            @Override
            public void setCategoryTempBypassTicks(@NotNull UUID uuid, @NotNull String category, long ticks) {
                DarknessUser user = getUser(uuid);
                if (user != null) user.setCategoryTempBypassTicks(category, ticks);
            }

            @Override
            public boolean isBypassing(@NotNull UUID uuid) {
                DarknessUser user = getUser(uuid);
                return user != null && user.isBypassing();
            }

            @Override
            public boolean isBypassing(@NotNull UUID uuid, @NotNull String checkName) {
                DarknessUser user = getUser(uuid);
                return user != null && user.isBypassing(checkName);
            }

            @Override
            public void resetBypass(@NotNull UUID uuid) {
                DarknessUser user = getUser(uuid);
                if (user != null) user.resetBypass();
            }
        };
    }

    @NotNull
    default PunishmentManager getPunishmentManager() {
        return new PunishmentManager() {
            @Override
            public void punish(@NotNull UUID uuid, @NotNull String reason) {
                DarknessAbstractAPI.this.punish(uuid, reason);
            }

            @Override
            public void punish(@NotNull String playerName, @NotNull String reason) {
                DarknessAbstractAPI.this.punish(playerName, reason);
            }

            @Override
            public boolean registerAnimation(@NotNull String name, @NotNull PunishAnimation animation) {
                return DarknessAPI.registerAnimation(name, animation);
            }

            @Override
            public boolean registerAnimation(@NotNull String name, @NotNull AnimationMode animation) {
                return DarknessAPI.registerAnimation(name, animation);
            }

            @Override
            public boolean unregisterAnimation(@NotNull String name) {
                return DarknessAPI.unregisterAnimation(name);
            }

            @Override
            public PunishAnimation getAnimation(@NotNull String name) {
                return DarknessAPI.getAnimation(name);
            }
        };
    }

    @NotNull
    default ViolationManager getViolationManager() {
        return new ViolationManager() {
            @Override
            public double getTotalViolations(@NotNull UUID uuid) {
                DarknessUser user = getUser(uuid);
                return user != null ? user.getTotalViolations() : 0.0;
            }

            @Override
            public double getCombatViolations(@NotNull UUID uuid) {
                DarknessUser user = getUser(uuid);
                return user != null ? user.getCombatViolations() : 0.0;
            }

            @Override
            public double getMovementViolations(@NotNull UUID uuid) {
                DarknessUser user = getUser(uuid);
                return user != null ? user.getMovementViolations() : 0.0;
            }

            @Override
            public double getWorldViolations(@NotNull UUID uuid) {
                DarknessUser user = getUser(uuid);
                return user != null ? user.getWorldViolations() : 0.0;
            }

            @NotNull
            @Override
            public Collection<CheckData> getCheckData(@NotNull UUID uuid) {
                DarknessUser user = getUser(uuid);
                return user != null ? user.getCheckData() : Collections.emptyList();
            }

            @Override
            public boolean resetViolations(@NotNull UUID uuid, @NotNull String check) {
                DarknessUser user = getUser(uuid);
                return user != null && user.resetViolations(check);
            }

            @Override
            public boolean resetViolations(@NotNull UUID uuid, @NotNull String check, @NotNull String type) {
                DarknessUser user = getUser(uuid);
                return user != null && user.resetViolations(check, type);
            }

            @Override
            public boolean simulateFlag(@NotNull UUID uuid, @NotNull String check, @NotNull String type, @NotNull String verbose) {
                DarknessUser user = getUser(uuid);
                return user != null && user.flag(check, type, verbose);
            }
        };
    }

    default double getTotalViolations(Player player) {
        DarknessUser user = getUser(player);
        return user != null ? user.getTotalViolations() : 0.0;
    }
}
