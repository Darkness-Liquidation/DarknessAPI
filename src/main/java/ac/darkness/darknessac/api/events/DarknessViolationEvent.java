package ac.darkness.darknessac.api.events;

import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.DarknessUser;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DarknessViolationEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    @Getter private final String check;
    @Getter private final String type;
    @Getter private final String fullCheckName;
    @Getter private final String verbose;
    @Getter private final double violations;
    @Getter private final boolean experimental;
    private boolean cancelled = false;

    public DarknessViolationEvent(@NotNull Player player,
                                  @NotNull String check,
                                  @NotNull String type,
                                  @NotNull String fullCheckName,
                                  @NotNull String verbose,
                                  double violations,
                                  boolean experimental) {
        this(player, check, type, fullCheckName, verbose, violations, experimental,
                !Bukkit.isPrimaryThread());
    }

    public DarknessViolationEvent(@NotNull Player player,
                                  @NotNull String check,
                                  @NotNull String type,
                                  @NotNull String fullCheckName,
                                  @NotNull String verbose,
                                  double violations,
                                  boolean experimental,
                                  boolean isAsync) {
        super(isAsync);
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

    @Nullable
    public DarknessUser getUser() {
        return DarknessAPI.INSTANCE != null
                ? DarknessAPI.INSTANCE.getUser(player)
                : DarknessAPI.getUser(player);
    }

    public int getPing() {
        DarknessUser user = getUser();
        return user != null ? user.getTransactionPing() : 0;
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