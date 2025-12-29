package ac.darkness.darknessac.api.event.events;

import ac.darkness.darknessac.api.AbstractCheck;
import ac.darkness.darknessac.api.DarknessUser;

public class CommandExecuteEvent extends DarknessVerboseCheckEvent {
    private final String command;

    public CommandExecuteEvent(DarknessUser player, AbstractCheck check, String verbose, String command) {
        super(player, check, verbose);
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
