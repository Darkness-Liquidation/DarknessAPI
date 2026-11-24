package ac.darkness.darknessac.api.events;

import ac.darkness.darknessac.api.CheckCategory;
import ac.darkness.darknessac.api.CheckType;
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

/**
 * Fired immediately before an anticheat setback action is applied to a player.
 * Cancelling this event prevents the setback (teleport, attack cancel, inventory close)
 * from being executed, while keeping violation telemetry and alerts intact.
 */
public final class DarknessSetbackEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    @Getter private final String check;
    @Getter private final String type;
    @Getter private final String fullCheckName;
    @Getter private final double violations;
    @Getter private final double setbackVL;
    private boolean cancelled = false;

    public DarknessSetbackEvent(@NotNull Player player,
                                @NotNull String check,
                                @NotNull String type,
                                @NotNull String fullCheckName,
                                double violations,
                                double setbackVL) {
        this(player, check, type, fullCheckName, violations, setbackVL, !Bukkit.isPrimaryThread());
    }

    public DarknessSetbackEvent(@NotNull Player player,
                                @NotNull String check,
                                @NotNull String type,
                                @NotNull String fullCheckName,
                                double violations,
                                double setbackVL,
                                boolean isAsync) {
        super(isAsync);
        this.player = player;
        this.check = check;
        this.type = type;
        this.fullCheckName = fullCheckName;
        this.violations = violations;
        this.setbackVL = setbackVL;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @Nullable
    public DarknessUser getUser() {
        return DarknessAPI.getUser(player);
    }

    @NotNull
    public CheckType getCheckType() {
        return CheckType.fromString(check);
    }

    @NotNull
    public CheckCategory getCategory() {
        return getCheckType().category();
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
