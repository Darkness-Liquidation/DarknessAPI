package ac.darkness.darknessac.api.plugin;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface DarknessPluginDescription {
    String getVersion();

    String getDescription();

    public @NotNull Collection<String> getAuthors();
}
