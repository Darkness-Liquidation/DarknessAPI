package ac.darkness.darknessac.api.events;

import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.TrustLevel;
import ac.darkness.darknessac.api.event.DarknessEvent;
import ac.darkness.darknessac.api.event.DarknessUserEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DarknessTrustChangeEvent extends DarknessEvent implements DarknessUserEvent {
    @Getter private final DarknessUser user;
    @Getter private final TrustLevel oldLevel;
    @Getter private final TrustLevel newLevel;
    @Getter private final int trustScore;

    public DarknessTrustChangeEvent(@NotNull DarknessUser user,
                                    @NotNull TrustLevel oldLevel,
                                    @NotNull TrustLevel newLevel,
                                    int trustScore) {
        super(true);
        this.user = user;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
        this.trustScore = trustScore;
    }

    public String getUsername() {
        return user.getName();
    }

    public UUID getPlayerUUID() {
        return user.getUniqueId();
    }

    public int getPing() {
        return user.getTransactionPing();
    }

    public int getTrustPoints() {
        return user.getTrustPoints();
    }

    public double getTotalViolations() {
        return user.getTotalViolations();
    }

    @Nullable
    public Player getPlayer() {
        try {
            return Bukkit.getPlayer(user.getUniqueId());
        } catch (Throwable ignored) {
            return null;
        }
    }
}
