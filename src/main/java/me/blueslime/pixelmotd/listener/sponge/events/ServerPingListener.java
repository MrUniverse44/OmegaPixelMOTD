package me.blueslime.pixelmotd.listener.sponge.events;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.Ping;
import me.blueslime.pixelmotd.motd.builder.PingBuilder;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.server.ClientPingServerEvent;

public class ServerPingListener implements Ping {

    private final PixelMOTD<Server> plugin;

    public ServerPingListener(PixelMOTD<Server> plugin) {
        this.plugin = plugin;
    }

    @Listener
    public void onClientPingServer(ClientPingServerEvent event) {

    }

    public void update() {

    }

    public PingBuilder<?, ?, ?, ?> getPingBuilder() {
        return null;
    }
}
