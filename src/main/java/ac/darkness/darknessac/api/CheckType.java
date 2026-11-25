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
        if (name == null || name.isBlank()) return false;
        String clean = name.trim().replaceAll("[\\s()_.-]", "");
        String selfClean = this.checkName.replaceAll("[\\s()_.-]", "");
        if (this.name().equalsIgnoreCase(name)
                || this.name().equalsIgnoreCase(clean)
                || this.checkName.equalsIgnoreCase(name)
                || selfClean.equalsIgnoreCase(clean)) {
            return true;
        }
        int paren = name.indexOf('(');
        if (paren > 0) {
            String base = name.substring(0, paren).trim();
            if (this.name().equalsIgnoreCase(base) || this.checkName.equalsIgnoreCase(base)) {
                return true;
            }
        }
        int dot = name.indexOf('.');
        if (dot > 0) {
            String base = name.substring(0, dot).trim();
            if (this.name().equalsIgnoreCase(base) || this.checkName.equalsIgnoreCase(base)) {
                return true;
            }
        }
        return clean.toLowerCase().startsWith(selfClean.toLowerCase())
                || clean.toLowerCase().startsWith(this.name().toLowerCase());
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
        String trimmed = name.trim();
        String base = trimmed;
        if (trimmed.contains("(")) {
            base = trimmed.substring(0, trimmed.indexOf('(')).trim();
        } else if (trimmed.contains(".")) {
            base = trimmed.substring(0, trimmed.indexOf('.')).trim();
        }
        for (CheckType type : values()) {
            if (type != OTHER && (type.matches(trimmed) || type.matches(base))) {
                return type;
            }
        }
        return OTHER;
    }
}
