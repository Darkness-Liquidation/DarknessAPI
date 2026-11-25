package ac.darkness.darknessac.api.events;

import ac.darkness.darknessac.api.AbstractCheck;
import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.TrustLevel;
import ac.darkness.darknessac.api.event.DarknessCheckEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DarknessSetbackEvent extends DarknessCheckEvent {
    @Getter private final String checkName;
    @Getter private final String checkType;
    @Getter private final String fullCheckName;
    @Getter private final double violations;
    @Getter private final double setbackVL;

    public DarknessSetbackEvent(@NotNull DarknessUser user,
                                @Nullable AbstractCheck checkInstance,
                                @NotNull String checkName,
                                @NotNull String checkType,
                                @NotNull String fullCheckName,
                                double violations,
                                double setbackVL) {
        super(user, checkInstance, true);
        this.checkName = checkName;
        this.checkType = checkType;
        this.fullCheckName = fullCheckName;
        this.violations = violations;
        this.setbackVL = setbackVL;
    }

    public DarknessSetbackEvent(@NotNull DarknessUser user,
                                @NotNull String checkName,
                                @NotNull String checkType,
                                @NotNull String fullCheckName,
                                double violations,
                                double setbackVL) {
        this(user, null, checkName, checkType, fullCheckName, violations, setbackVL);
    }

    @Nullable
    public AbstractCheck getCheck() {
        return getCheckInstance();
    }

    public UUID getPlayerUUID() {
        return getUser().getUniqueId();
    }

    public String getUsername() {
        return getUser().getName();
    }

    public int getPing() {
        return getUser().getTransactionPing();
    }

    public TrustLevel getTrustLevel() {
        return getUser().getTrustLevel();
    }

    public int getTrustScore() {
        return getUser().getTrustScore();
    }

    public double getTotalViolations() {
        return getUser().getTotalViolations();
    }

    @Nullable
    public Player getPlayer() {
        try {
            return Bukkit.getPlayer(getUser().getUniqueId());
        } catch (Throwable ignored) {
            return null;
        }
    }
}
