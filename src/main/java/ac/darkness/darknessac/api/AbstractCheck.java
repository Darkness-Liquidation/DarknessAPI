package ac.darkness.darknessac.api;

public interface AbstractCheck extends AbstractProcessor {

    String getCheckName();

    default String getAlternativeName() {
        return getCheckName();
    }

    double getViolations();

    default void resetViolations() {}

    default void setViolations(double violations) {}

    default double getBuffer() {
        return 0.0;
    }

    default void setBuffer(double buffer) {}

    default void resetBuffer() {
        setBuffer(0.0);
    }

    default double increaseBuffer() {
        return 0.0;
    }

    default double increaseBuffer(double amount) {
        return 0.0;
    }

    default void decreaseBuffer(double decay) {}

    default boolean flag(String verbose) {
        return false;
    }

    long getLastViolationTime();

    double getDecay();

    double getSetbackVL();

    boolean isExperimental();

    default boolean isEnabled() {
        return true;
    }

    default void setEnabled(boolean enabled) {}
}
