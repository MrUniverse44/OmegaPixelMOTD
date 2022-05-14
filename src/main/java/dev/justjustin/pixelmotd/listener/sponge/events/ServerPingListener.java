package dev.justjustin.pixelmotd.listener.sponge.events;

import dev.mruniverse.slimelib.SlimePlugin;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.server.ClientPingServerEvent;

public class ServerPingListener {

    private final SlimePlugin<Server> slimePlugin;

    public ServerPingListener(SlimePlugin<Server> slimePlugin) {
        this.slimePlugin = slimePlugin;
    }

    @Listener
    public void onClientPingServer(ClientPingServerEvent event) {

    }
}
