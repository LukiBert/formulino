package pl.edu.wat.wcy.edp.bd.formulino.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class EventBus {
    private static EventBus instance;
    private final HashMap<Class<?>, Set<Consumer<?>>> listeners = new HashMap<>();

    private EventBus() {}

    public static synchronized EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    public <T> void subscribe(Class<T> eventType, Consumer<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new HashSet<>()).add(listener);
    }

    public <T> void publish(T event) {
        Set<Consumer<?>> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            for (Consumer<?> listener : eventListeners) {
                ((Consumer<T>) listener).accept(event);
            }
        }
    }
}
