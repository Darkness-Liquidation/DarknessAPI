package ac.darkness.darknessac.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Standard enum of anticheat check types and their associated category.
 */
public enum CheckType {

    AIM("Aim", CheckCategory.COMBAT),
    ANALYSIS("Analysis", CheckCategory.COMBAT),
    REACH("Reach", CheckCategory.COMBAT),
    HITBOX("Hitbox", CheckCategory.COMBAT),
    AUTOCLICKER("AutoClicker", CheckCategory.COMBAT),
    BADPACKETS("BadPackets", CheckCategory.WORLD),
    PROTOCOL("Protocol", CheckCategory.WORLD),
    CRASH("Crash", CheckCategory.WORLD),
    EXPLOIT("Exploit", CheckCategory.WORLD),
    CHAT("Chat", CheckCategory.WORLD),
    AUTOACTION("AutoAction", CheckCategory.WORLD),
    FREECAM("FreeCam", CheckCategory.WORLD),
    KILLAURA("KillAura", CheckCategory.COMBAT),
    SCAFFOLD("Scaffold", CheckCategory.WORLD),
    NOFALL("NoFall", CheckCategory.MOVEMENT),
    BARITONE("Baritone", CheckCategory.MOVEMENT),
    VEHICLE("Vehicle", CheckCategory.MOVEMENT),
    PREDICTION("Prediction", CheckCategory.MOVEMENT),
    ELYTRA("Elytra", CheckCategory.MOVEMENT),
    TIMER("Timer", CheckCategory.WORLD),
    SPRINT("Sprint", CheckCategory.MOVEMENT),
    NOSLOW("NoSlow", CheckCategory.MOVEMENT),
    VELOCITY("Velocity", CheckCategory.COMBAT),
    INVENTORY("Inventory", CheckCategory.WORLD),
    INTERACT("Interact", CheckCategory.WORLD),
    OTHER("Other", CheckCategory.OTHER);

    private final String checkName;
    private final CheckCategory category;

    CheckType(@NotNull String checkName, @NotNull CheckCategory category) {
        this.checkName = checkName;
        this.category = category;
    }

    @NotNull
    public String checkName() {
        return checkName;
    }

    @NotNull
    public String getCheckName() {
        return checkName;
    }

    @NotNull
    public CheckCategory category() {
        return category;
    }

    @NotNull
    public CheckCategory getCategory() {
        return category;
    }

    @NotNull
    public CheckCategory getCheckCategory() {
        return category;
    }

    /**
     * Tests whether the provided string matches this check type.
     *
     * @param name check type name string
     * @return true if matching
     */
    public boolean matches(@Nullable String name) {
        if (name == null) return false;
        String clean = name.trim().replaceAll("[\\s()_.-]", "");
        return this.name().equalsIgnoreCase(name)
                || this.name().equalsIgnoreCase(clean)
                || this.checkName.equalsIgnoreCase(name)
                || this.checkName.replaceAll("[\\s()_.-]", "").equalsIgnoreCase(clean);
    }

    /**
     * Resolves a CheckType enum from a string representation, defaulting to OTHER.
     *
     * @param name check name string
     * @return matching CheckType or OTHER
     */
    @NotNull
    public static CheckType fromString(@Nullable String name) {
        if (name == null || name.isBlank()) return OTHER;
        String clean = name.trim().replaceAll("[\\s()_.-]", "");
        for (CheckType type : values()) {
            if (type.matches(name) || type.matches(clean)) {
                return type;
            }
        }
        return OTHER;
    }
}
