package ac.darkness.darknessac.api;

/**
 * Player trust level based on anticheat violations and activity.
 */
public enum TrustLevel {

    /**
     * Clean player with few or no violations.
     */
    GREEN,

    /**
     * Moderately suspicious player with minor/accumulating violations.
     */
    ORANGE,

    /**
     * Highly suspicious player exceeding violation thresholds.
     */
    RED
}
