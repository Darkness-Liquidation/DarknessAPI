package ac.darkness.darknessac.api.event;

@FunctionalInterface
public interface DarknessEventListener<T extends DarknessEvent> {
    void handle(T event) throws Exception;
}