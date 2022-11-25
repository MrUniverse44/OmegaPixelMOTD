package me.blueslime.pixelmotd;

import me.blueslime.pixelmotd.motd.listener.Ping;
import me.blueslime.pixelmotd.motd.listener.platforms.BungeeListenerManager;
import me.blueslime.pixelmotd.motd.listener.platforms.SpigotListenerManager;
import me.blueslime.pixelmotd.motd.listener.platforms.SpongeListenerManager;
import me.blueslime.pixelmotd.motd.listener.platforms.VelocityListenerManager;
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
