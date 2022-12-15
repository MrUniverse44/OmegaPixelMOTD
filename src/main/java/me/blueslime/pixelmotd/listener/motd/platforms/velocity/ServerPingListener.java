package me.blueslime.pixelmotd.listener.motd.platforms.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.motd.MotdListener;

public class ServerPingListener extends MotdListener<ProxyServer> {

    public ServerPingListener(PixelMOTD<ProxyServer> plugin) {
        super(plugin);
    }



    @Override
    public void register() {

    }
}
