package ac.darkness.darknessac.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * High-level categorization of anticheat checks.
 */
public enum CheckCategory {

    /**
     * Combat checks (KillAura, Reach, Hitbox, AutoClicker, Aim, Analysis, Velocity).
     */
    COMBAT("Combat"),

    /**
     * Movement checks (Speed, Fly, NoFall, Sprint, NoSlow, Elytra, Prediction).
     */
    MOVEMENT("Movement"),

    /**
     * World and packet checks (Scaffold, Timer, BadPackets, Protocol, FastBreak).
     */
    WORLD("World"),

    /**
     * Miscellaneous or uncategorized checks.
     */
    OTHER("Other");

    private final String displayName;

    CheckCategory(@NotNull String displayName) {
        this.displayName = displayName;
    }

    @NotNull
    public String getDisplayName() {
        return displayName;
    }

    @NotNull
    public String displayName() {
        return displayName;
    }

    /**
     * Tests whether the provided string matches this category by name or display name.
     *
     * @param name category name string
     * @return true if matching
     */
    public boolean matches(@Nullable String name) {
        if (name == null) return false;
        return this.name().equalsIgnoreCase(name) || this.displayName.equalsIgnoreCase(name);
    }

    /**
     * Resolves a CheckCategory enum from a string representation, defaulting to OTHER.
     *
     * @param name category name string
     * @return matching CheckCategory or OTHER
     */
    @NotNull
    public static CheckCategory fromString(@Nullable String name) {
        if (name == null) return OTHER;
        for (CheckCategory category : values()) {
            if (category.matches(name)) {
                return category;
            }
        }
        return OTHER;
    }
}
