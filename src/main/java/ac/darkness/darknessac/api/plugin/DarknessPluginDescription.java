package ac.darkness.darknessac.api.plugin;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface DarknessPluginDescription {
    String version();

    String description();

    @NotNull Collection<String> authors();

    default String getVersion() {
        return version();
    }

    default String getDescription() {
        return description();
    }

    default @NotNull Collection<String> getAuthors() {
        return authors();
    }
}
