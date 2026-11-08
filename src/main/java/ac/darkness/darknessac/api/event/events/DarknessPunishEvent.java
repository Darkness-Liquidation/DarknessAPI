package ac.darkness.darknessac.api.event.events;

import ac.darkness.darknessac.api.AbstractCheck;
import ac.darkness.darknessac.api.DarknessUser;
import ac.darkness.darknessac.api.event.Cancellable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DarknessPunishEvent extends DarknessCheckEvent implements Cancellable {
    private String reason;
    private final double violations;
    private List<String> commands;
    private boolean cancelled = false;

    public DarknessPunishEvent(@NotNull DarknessUser user, @Nullable AbstractCheck check, @NotNull String reason, double violations, @NotNull List<String> commands) {
        super(user, check);
        this.reason = reason;
        this.violations = violations;
        this.commands = new ArrayList<>(commands);
    }

    public DarknessPunishEvent(@NotNull DarknessUser user, @Nullable AbstractCheck check, @NotNull String reason, double violations) {
        this(user, check, reason, violations, Collections.emptyList());
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getViolations() {
        return violations;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands != null ? new ArrayList<>(commands) : new ArrayList<>();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}