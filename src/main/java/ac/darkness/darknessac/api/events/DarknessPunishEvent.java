package ac.darkness.darknessac.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DarknessPunishEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private String reason;
    private List<String> commands;
    private final double violations;
    private boolean cancelled = false;

    public DarknessPunishEvent(@NotNull Player player, @NotNull String reason, @NotNull List<String> commands, double violations) {
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands != null ? new ArrayList<>(commands) : new ArrayList<>();
    }

    public double getViolations() {
        return violations;
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