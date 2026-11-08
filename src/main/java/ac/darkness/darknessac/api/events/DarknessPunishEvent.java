package ac.darkness.darknessac.api.events;

import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.DarknessUser;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class DarknessPunishEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    @Setter @Getter private String reason;
    @Getter private List<String> commands;
    @Getter private final double violations;
    private boolean cancelled = false;

    public DarknessPunishEvent(@NotNull Player player,
                               @NotNull String reason,
                               @NotNull List<String> commands,
                               double violations) {
        this(player, reason, commands, violations, !Bukkit.isPrimaryThread());
    }

    public DarknessPunishEvent(@NotNull Player player,
                               @NotNull String reason,
                               @NotNull List<String> commands,
                               double violations,
                               boolean isAsync) {
        super(isAsync);
        this.player = player;
        this.reason = reason;
        this.commands = new ArrayList<>(commands);
        this.violations = violations;
    }

    public DarknessPunishEvent(@NotNull Player player, @NotNull String reason, @NotNull List<String> commands) {
        this(player, reason, commands, 0.0);
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @Nullable
    public DarknessUser getUser() {
        return DarknessAPI.INSTANCE != null
                ? DarknessAPI.INSTANCE.getUser(player)
                : DarknessAPI.getUser(player);
    }

    public void setCommands(List<String> commands) {
        this.commands = commands != null ? new ArrayList<>(commands) : new ArrayList<>();
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