package dev.justjustin.pixelmotd;

import dev.justjustin.pixelmotd.listener.bungeecord.BungeeListenerManager;
import dev.justjustin.pixelmotd.listener.spigot.SpigotListenerManager;
import dev.justjustin.pixelmotd.listener.sponge.SpongeListenerManager;
import dev.justjustin.pixelmotd.listener.velocity.VelocityListenerManager;
import dev.mruniverse.slimelib.SlimePlatform;

public interface ListenerManager {

    /**
     * Create a new instance for ListenerManager depending on the platform
     */
    static <T> ListenerManager createNewInstance(SlimePlatform platform, PixelMOTD<T> plugin) {
        switch (platform) {
            case SPIGOT:
                return new SpigotListenerManager(plugin);
            default:
            case BUNGEECORD:
                return new BungeeListenerManager(plugin);
            case SPONGE:
                return new SpongeListenerManager(plugin);
            case VELOCITY:
                return new VelocityListenerManager(plugin);
        }
    }

    /**
     * Register the default listener
     */
    void register();



}
