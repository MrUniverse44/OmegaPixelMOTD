package me.blueslime.omegapixelmotd.modules.listeners;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Listeners {
    private final Map<Class<? extends PluginListener>, PluginListener> listenerMap = new ConcurrentHashMap<>();

    public void registerListener(PluginListener... listeners) {
        for (PluginListener listener : listeners) {
            listenerMap.put(
                    listener.getClass(),
                    listener
            );
        }
    }

    public void initialize() {
        for (PluginListener listener : listenerMap.values()) {
            listener.initialize();
        }
    }

    public void shutdown() {
        for (PluginListener listener : listenerMap.values()) {
            listener.shutdown();
        }
    }

    public void reload() {
        for (PluginListener listener : listenerMap.values()) {
            listener.reload();
        }
    }
}
