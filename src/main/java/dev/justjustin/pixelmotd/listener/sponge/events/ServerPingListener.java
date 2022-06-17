package dev.justjustin.pixelmotd.listener.sponge.events;

import dev.mruniverse.slimelib.SlimePlugin;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.server.ClientPingServerEvent;

public class ServerPingListener {

    private final SlimePlugin<Server> slimePlugin;

    public ServerPingListener(SlimePlugin<Server> slimePlugin, SlimeLogs logs) {
        this.slimePlugin = slimePlugin;
    }

    @Listener
    public void onClientPingServer(ClientPingServerEvent event) {

    }

    public void update() {

    }
}
