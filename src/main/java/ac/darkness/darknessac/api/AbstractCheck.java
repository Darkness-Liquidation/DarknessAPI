package ac.darkness.darknessac.api;

import ac.darkness.darknessac.api.common.BasicStatus;

public interface AbstractCheck extends AbstractProcessor, BasicStatus {

    String getCheckName();

    default String getAlternativeName() {
        return getCheckName();
    }

    double getViolations();

    /**
     * Resets violations for this check back to 0.
     */
    default void resetViolations() {}

    /**
     * Sets violations for this check.
     *
     * @param violations New violations count
     */
    default void setViolations(double violations) {}

    /**
     * Triggers a violation alert for this check with custom verbose information.
     *
     * @param verbose Details of the violation
     * @return true if alert was sent successfully
     */
    default boolean flag(String verbose) {
        return false;
    }

    /**
     * Returns the time of the last violation in UTC milliseconds or 0 if no violations have occurred.
     * Internally uses {@link System#currentTimeMillis()} when a violation occurs.
     * @return the time of the last violation in UTC milliseconds
     */
    long getLastViolationTime();

    double getDecay();

    double getSetbackVL();

    boolean isExperimental();

}
