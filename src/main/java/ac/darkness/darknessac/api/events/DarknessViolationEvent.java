package ac.darkness.darknessac.api.events;

import ac.darkness.darknessac.api.AbstractCheck;
import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.TrustLevel;
import ac.darkness.darknessac.api.event.DarknessVerboseCheckEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DarknessViolationEvent extends DarknessVerboseCheckEvent {
    @Getter private final String check;
    @Getter private final String type;
    @Getter private final String fullCheckName;
    @Getter private final String description;
    @Getter private final double violations;
    @Getter private final boolean experimental;

    public DarknessViolationEvent(@NotNull DarknessUser user,
                                  @Nullable AbstractCheck checkInstance,
                                  @NotNull String check,
                                  @NotNull String type,
                                  @NotNull String fullCheckName,
                                  @NotNull String description,
                                  double violations,
                                  boolean experimental,
                                  @NotNull String verbose) {
        super(user, checkInstance, verbose, true);
        this.check = check;
        this.type = type;
        this.fullCheckName = fullCheckName;
        this.description = description;
        this.violations = violations;
        this.experimental = experimental;
    }

    public DarknessViolationEvent(@NotNull DarknessUser user,
                                  @NotNull String check,
                                  @NotNull String type,
                                  @NotNull String fullCheckName,
                                  @NotNull String verbose,
                                  double violations,
                                  boolean experimental) {
        this(user, null, check, type, fullCheckName, "", violations, experimental, verbose);
    }

    @NotNull
    public String getCheckName() {
        return check;
    }

    public UUID getPlayerUUID() {
        return getUser().getUniqueId();
    }

    public String getUsername() {
        return getUser().getName();
    }

    @Nullable
    public Player getPlayer() {
        try {
            return Bukkit.getPlayer(getUser().getUniqueId());
        } catch (Throwable ignored) {
            return null;
        }
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

    public boolean isSetback() {
        AbstractCheck checkInstance = getCheckInstance();
        return checkInstance != null && violations > checkInstance.getSetbackVL();
    }

    public double getSetbackVL() {
        AbstractCheck checkInstance = getCheckInstance();
        return checkInstance != null ? checkInstance.getSetbackVL() : 0.0;
    }

    public double getDecay() {
        AbstractCheck checkInstance = getCheckInstance();
        return checkInstance != null ? checkInstance.getDecay() : 0.0;
    }

    public double getBuffer() {
        AbstractCheck checkInstance = getCheckInstance();
        return checkInstance != null ? checkInstance.getBuffer() : 0.0;
    }
}
