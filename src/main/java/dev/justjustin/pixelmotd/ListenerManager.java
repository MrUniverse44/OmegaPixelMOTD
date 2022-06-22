package dev.justjustin.pixelmotd;

import dev.justjustin.pixelmotd.listener.Ping;
import dev.justjustin.pixelmotd.listener.bungeecord.BungeeListenerManager;
import dev.justjustin.pixelmotd.listener.spigot.SpigotListenerManager;
import dev.justjustin.pixelmotd.listener.sponge.SpongeListenerManager;
import dev.justjustin.pixelmotd.listener.velocity.VelocityListenerManager;
import dev.mruniverse.slimelib.SlimePlatform;
import dev.mruniverse.slimelib.logs.SlimeLogs;

public interface ListenerManager {

    /**
     * Create a new instance for ListenerManager depending on the platform
     */
    static <T> ListenerManager createNewInstance(SlimePlatform platform, PixelMOTD<T> plugin, SlimeLogs logs) {
        switch (platform) {
            case SPIGOT:
                return new SpigotListenerManager(plugin, logs);
            default:
            case BUNGEECORD:
                return new BungeeListenerManager(plugin, logs);
            case SPONGE:
                return new SpongeListenerManager(plugin, logs);
            case VELOCITY:
                return new VelocityListenerManager(plugin, logs);
        }
    }

    /**
     * Register the default listener
     */
    void register();

    void update();

    Ping getPing();



}
