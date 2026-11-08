package ac.darkness.darknessac.api.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public final class DarknessPunishWaveEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Collection<UUID> playerUUIDs;
    private final Collection<Player> onlinePlayers;
    private boolean cancelled = false;

    public DarknessPunishWaveEvent(@NotNull Collection<UUID> playerUUIDs, @NotNull Collection<Player> onlinePlayers) {
        this(playerUUIDs, onlinePlayers, !Bukkit.isPrimaryThread());
    }

    public DarknessPunishWaveEvent(@NotNull Collection<UUID> playerUUIDs,
                                   @NotNull Collection<Player> onlinePlayers,
                                   boolean isAsync) {
        super(isAsync);
        this.playerUUIDs = playerUUIDs;
        this.onlinePlayers = onlinePlayers;
    }

    public DarknessPunishWaveEvent(@NotNull Collection<UUID> playerUUIDs) {
        this(playerUUIDs, Collections.emptyList());
    }

    @NotNull
    public Collection<UUID> getPlayerUUIDs() {
        return playerUUIDs;
    }

    @NotNull
    public Collection<Player> getOnlinePlayers() {
        return onlinePlayers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}