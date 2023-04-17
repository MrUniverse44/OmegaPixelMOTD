package me.blueslime.pixelmotd.listener.sponge;

import me.blueslime.pixelmotd.listener.PluginListener;
import me.blueslime.pixelmotd.listener.sponge.player.LoginListener;
import me.blueslime.pixelmotd.listener.sponge.server.ServerPingListener;
import org.spongepowered.api.Server;

public enum SpongeListener {
    SERVER_PING(ServerPingListener.class),
    LOGIN(LoginListener.class);

    private final Class<? extends PluginListener<Server>> parent;

    SpongeListener(Class<? extends PluginListener<Server>> parent) {
        this.parent = parent;
    }
    SpongeListener() {
        this.parent = null;
    }

    public Class<? extends PluginListener<Server>> getParent() {
        return parent;
    }
}
}
