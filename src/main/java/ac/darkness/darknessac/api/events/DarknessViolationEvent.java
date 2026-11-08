package ac.darkness.darknessac.api.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class DarknessViolationEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    @Getter
    private final String check;
    @Getter
    private final String type;
    @Getter
    private final String fullCheckName;
    @Getter
    private final String verbose;
    @Getter
    private final double violations;
    @Getter
    private final boolean experimental;
    private boolean cancelled = false;

    public DarknessViolationEvent(@NotNull Player player,
                                  @NotNull String check,
                                  @NotNull String type,
                                  @NotNull String fullCheckName,
                                  @NotNull String verbose,
                                  double violations,
                                  boolean experimental) {
        this.player = player;
        this.check = check;
        this.type = type;
        this.fullCheckName = fullCheckName;
        this.verbose = verbose;
        this.violations = violations;
        this.experimental = experimental;
    }

    @NotNull
    public Player getPlayer() {
        return player;
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