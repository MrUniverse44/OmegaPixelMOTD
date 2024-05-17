package me.blueslime.omegapixelmotd.modules.listeners;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Listeners {
    private final Map<Class<? extends Listener>, Listener> listenerMap = new ConcurrentHashMap<>();

    public void registerListener(Listener... listeners) {
        for (Listener listener : listeners) {
            listenerMap.put(
                    listener.getClass(),
                    listener
            );
        }
    }

    public void initialize() {
        for (Listener listener : listenerMap.values()) {
            listener.initialize();
        }
    }

    public void shutdown() {
        for (Listener listener : listenerMap.values()) {
            listener.shutdown();
        }
    }

    public void reload() {
        for (Listener listener : listenerMap.values()) {
            listener.reload();
        }
    }
}
