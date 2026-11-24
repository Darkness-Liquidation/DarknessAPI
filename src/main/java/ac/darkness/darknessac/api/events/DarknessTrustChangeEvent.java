package ac.darkness.darknessac.api.events;

import ac.darkness.darknessac.api.DarknessAPI;
import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.TrustLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Fired when a player's anticheat Trust Factor level changes (e.g. GREEN -> ORANGE -> RED).
 * Allows servers to immediately notify staff, apply restrictions, or adjust matchmaking
 * without polling {@link DarknessUser#getTrustLevel()} periodically.
 */
public final class DarknessTrustChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    @Getter private final UUID uniqueId;
    @Getter private final String username;
    @Getter private final TrustLevel oldLevel;
    @Getter private final TrustLevel newLevel;
    @Getter private final int trustPoints;
    @Getter private final int trustScore;

    public DarknessTrustChangeEvent(@NotNull UUID uniqueId,
                                    @NotNull String username,
                                    @NotNull TrustLevel oldLevel,
                                    @NotNull TrustLevel newLevel,
                                    int trustPoints,
                                    int trustScore) {
        this(uniqueId, username, oldLevel, newLevel, trustPoints, trustScore, !Bukkit.isPrimaryThread());
    }

    public DarknessTrustChangeEvent(@NotNull UUID uniqueId,
                                    @NotNull String username,
                                    @NotNull TrustLevel oldLevel,
                                    @NotNull TrustLevel newLevel,
                                    int trustPoints,
                                    int trustScore,
                                    boolean isAsync) {
        super(isAsync);
        this.uniqueId = uniqueId;
        this.username = username;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
        this.trustPoints = trustPoints;
        this.trustScore = trustScore;
    }

    @Nullable
    public Player getPlayer() {
        return Bukkit.getPlayer(uniqueId);
    }

    @Nullable
    public DarknessUser getUser() {
        return DarknessAPI.getUser(uniqueId);
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
