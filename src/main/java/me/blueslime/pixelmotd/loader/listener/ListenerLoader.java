package me.blueslime.pixelmotd.loader.listener;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.loader.listener.type.BukkitListenerLoader;
import me.blueslime.pixelmotd.loader.listener.type.BungeeListenerLoader;
import me.blueslime.pixelmotd.loader.listener.type.SpongeListenerLoader;
import me.blueslime.pixelmotd.loader.listener.type.VelocityListenerLoader;

public abstract class ListenerLoader {
    public static ListenerLoader initialize(PixelMOTD<?> plugin) {
        switch (plugin.getServerType()) {
            case SPONGE:
                return new SpongeListenerLoader(plugin);
            default:
            case BUNGEECORD:
                return new BungeeListenerLoader(plugin);
            case BUKKIT:
                return new BukkitListenerLoader(plugin);
            case VELOCITY:
                return new VelocityListenerLoader(plugin);
        }
    }
}
