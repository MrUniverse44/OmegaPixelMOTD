package me.blueslime.pixelmotd.listener.motd.platforms.sponge;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.motd.MotdListener;
import org.spongepowered.api.Server;

public class ServerPingListener extends MotdListener<Server> {
    public ServerPingListener(PixelMOTD<Server> plugin) {
        super(plugin);
    }

    @Override
    public void register() {

    }
}
