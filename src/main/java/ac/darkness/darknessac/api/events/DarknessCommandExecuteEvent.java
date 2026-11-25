package ac.darkness.darknessac.api.events;

import ac.darkness.darknessac.api.AbstractCheck;
import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.TrustLevel;
import ac.darkness.darknessac.api.event.DarknessVerboseCheckEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DarknessCommandExecuteEvent extends DarknessVerboseCheckEvent {
    @Getter @Setter private String command;

    public DarknessCommandExecuteEvent(@NotNull DarknessUser user,
                                       @Nullable AbstractCheck check,
                                       @NotNull String verbose,
                                       @NotNull String command) {
        super(user, check, verbose, false);
        this.command = command;
    }

    @Nullable
    public AbstractCheck getCheck() {
        return getCheckInstance();
    }

    @NotNull
    public String getCheckName() {
        return getCheck() != null ? getCheck().getCheckName() : "Unknown";
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
