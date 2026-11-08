package ac.darkness.darknessac.api;

import ac.darkness.darknessac.api.alerts.AlertManager;
import ac.darkness.darknessac.api.common.BasicReloadable;
import ac.darkness.darknessac.api.config.ConfigManager;
import ac.darkness.darknessac.api.config.ConfigReloadable;
import ac.darkness.darknessac.api.event.EventBus;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

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
}
