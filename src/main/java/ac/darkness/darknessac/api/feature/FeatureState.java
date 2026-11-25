package ac.darkness.darknessac.api.feature;

public enum FeatureState {
    ENABLED,
    DISABLED,
    UNSET;

    public boolean isEnabled() {
        return this == ENABLED;
    }

    public boolean isExplicit() {
        return this != UNSET;
    }
}

