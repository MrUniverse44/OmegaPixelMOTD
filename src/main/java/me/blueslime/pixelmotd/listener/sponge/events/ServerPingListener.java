package me.blueslime.pixelmotd.listener.sponge.events;

import me.blueslime.pixelmotd.listener.Ping;
import me.blueslime.pixelmotd.listener.PingBuilder;
import dev.mruniverse.slimelib.SlimePlugin;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.server.ClientPingServerEvent;

public class ServerPingListener implements Ping {

    private final SlimePlugin<Server> slimePlugin;

    public ServerPingListener(SlimePlugin<Server> slimePlugin, SlimeLogs logs) {
        this.slimePlugin = slimePlugin;
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
