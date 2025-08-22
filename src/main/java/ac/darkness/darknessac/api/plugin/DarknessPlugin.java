package ac.darkness.darknessac.api.plugin;

import java.io.File;
import java.util.logging.Logger;

public interface DarknessPlugin {

    DarknessPluginDescription getDescription();

    Logger getLogger();

    File getDataFolder();
}
