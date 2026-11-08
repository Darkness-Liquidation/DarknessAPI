package ac.darkness.darknessac.api.event;

import ac.darkness.darknessac.api.plugin.DarknessPlugin;
import org.jetbrains.annotations.NotNull;

public interface EventBus {

    void registerAnnotatedListeners(@NotNull Object pluginContext, @NotNull Object listener);

    void registerAnnotatedListeners(@NotNull DarknessPlugin plugin, @NotNull Object listener);

    void registerStaticAnnotatedListeners(@NotNull Object pluginContext, @NotNull Class<?> clazz);

    void registerStaticAnnotatedListeners(@NotNull DarknessPlugin plugin, @NotNull Class<?> clazz);

    void unregisterListeners(@NotNull Object pluginContext, @NotNull Object listener);

    void unregisterListeners(@NotNull DarknessPlugin plugin, @NotNull Object listener);

    void unregisterStaticListeners(@NotNull Object pluginContext, @NotNull Class<?> clazz);

    void unregisterStaticListeners(@NotNull DarknessPlugin plugin, @NotNull Class<?> clazz);

    void unregisterAllListeners(@NotNull Object pluginContext);

    void unregisterAllListeners(@NotNull DarknessPlugin plugin);

    void unregisterListener(@NotNull Object pluginContext, @NotNull DarknessEventListener<?> listener);

    void unregisterListener(@NotNull DarknessPlugin plugin, @NotNull DarknessEventListener<?> listener);

    void post(@NotNull DarknessEvent event);

    <T extends DarknessEvent> void subscribe(@NotNull Object pluginContext, @NotNull Class<T> eventType,
                                             @NotNull DarknessEventListener<T> listener, int priority,
                                             boolean ignoreCancelled, @NotNull Class<?> declaringClass);

    <T extends DarknessEvent> void subscribe(@NotNull DarknessPlugin plugin, @NotNull Class<T> eventType,
                                             @NotNull DarknessEventListener<T> listener, int priority,
                                             boolean ignoreCancelled, @NotNull Class<?> declaringClass);

    default <T extends DarknessEvent> void subscribe(@NotNull Object pluginContext, @NotNull Class<T> eventType,
                                                     @NotNull DarknessEventListener<T> listener, int priority,
                                                     boolean ignoreCancelled) {
        subscribe(pluginContext, eventType, listener, priority, ignoreCancelled, listener.getClass());
    }

    default <T extends DarknessEvent> void subscribe(@NotNull DarknessPlugin plugin, @NotNull Class<T> eventType,
                                                     @NotNull DarknessEventListener<T> listener, int priority,
                                                     boolean ignoreCancelled) {
        subscribe(plugin, eventType, listener, priority, ignoreCancelled, listener.getClass());
    }

    default <T extends DarknessEvent> void subscribe(@NotNull Object pluginContext, @NotNull Class<T> eventType,
                                                     @NotNull DarknessEventListener<T> listener) {
        subscribe(pluginContext, eventType, listener, 0, false);
    }

    default <T extends DarknessEvent> void subscribe(@NotNull DarknessPlugin plugin, @NotNull Class<T> eventType,
                                                     @NotNull DarknessEventListener<T> listener) {
        subscribe(plugin, eventType, listener, 0, false);
    }
}
