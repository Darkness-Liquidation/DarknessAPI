package ac.darkness.darknessac.api.plugin;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.logging.Logger;

public class BasicDarknessPlugin implements DarknessPlugin {
    private final Logger logger;
    private final File dataFolder;
    private final BasicDarknessPluginDescription description;

    public BasicDarknessPlugin(Logger logger, File dataFolder, String version, String description, Collection<String> authors) {
        this.logger = logger;
        this.dataFolder = dataFolder;
        this.description = new BasicDarknessPluginDescription(version, description, authors);
    }

    @Override
    public DarknessPluginDescription getDescription() {
        return description;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public File getDataFolder() {
        return dataFolder;
    }

    private record BasicDarknessPluginDescription(String version, String description,
                                                  Collection<String> authors) implements DarknessPluginDescription {

        @Override
            public @NotNull Collection<String> authors() {
                return authors;
            }
        }
}
