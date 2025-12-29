package ac.darkness.darknessac.api.config;

public interface ChangeableConfig extends ConfigManager {

    void set(String key, Object value);

}
