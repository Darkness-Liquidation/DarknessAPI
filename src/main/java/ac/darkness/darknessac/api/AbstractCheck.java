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
     * Gets the current buffer value of this check.
     *
     * @return current buffer
     */
    default double getBuffer() {
        return 0.0;
    }

    /**
     * Sets the buffer for this check.
     *
     * @param buffer New buffer value
     */
    default void setBuffer(double buffer) {}

    /**
     * Resets the buffer for this check back to 0.
     */
    default void resetBuffer() {
        setBuffer(0.0);
    }

    /**
     * Increases the buffer by 1.0.
     *
     * @return new buffer value
     */
    default double increaseBuffer() {
        return 0.0;
    }

    /**
     * Increases the buffer by the given amount.
     *
     * @param amount amount to increase
     * @return new buffer value
     */
    default double increaseBuffer(double amount) {
        return 0.0;
    }

    /**
     * Decreases the buffer by the given decay amount.
     *
     * @param decay amount to decrease
     */
    default void decreaseBuffer(double decay) {}

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
