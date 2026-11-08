package ac.darkness.darknessac.api.plugin;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface DarknessPluginDescription {
    String version();

    String description();

    public @NotNull Collection<String> authors();
}
