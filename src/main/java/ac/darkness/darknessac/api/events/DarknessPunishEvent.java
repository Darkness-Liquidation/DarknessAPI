package ac.darkness.darknessac.api.events;

import ac.darkness.darknessac.api.AbstractCheck;
import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.TrustLevel;
import ac.darkness.darknessac.api.event.DarknessCheckEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DarknessPunishEvent extends DarknessCheckEvent {
    @Setter @Getter private String reason;
    @Getter private List<String> commands;
    @Getter private final double violations;

    public DarknessPunishEvent(@NotNull DarknessUser user,
                               @Nullable AbstractCheck check,
                               @NotNull String reason,
                               @NotNull List<String> commands,
                               double violations) {
        super(user, check, false);
        this.reason = reason;
        this.commands = new ArrayList<>(commands);
        this.violations = violations;
    }

    public DarknessPunishEvent(@NotNull DarknessUser user,
                               @Nullable AbstractCheck check,
                               @NotNull String reason,
                               double violations,
                               @NotNull List<String> commands) {
        this(user, check, reason, commands, violations);
    }

    public DarknessPunishEvent(@NotNull DarknessUser user,
                               @NotNull String reason,
                               @NotNull List<String> commands,
                               double violations) {
        this(user, null, reason, commands, violations);
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

    public void setCommands(List<String> commands) {
        this.commands = commands != null ? new ArrayList<>(commands) : new ArrayList<>();
    }
}
