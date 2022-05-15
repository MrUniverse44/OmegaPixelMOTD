package dev.justjustin.pixelmotd.listener.bungeecord.events;

import dev.justjustin.pixelmotd.listener.Ping;
import dev.mruniverse.slimelib.SlimePlugin;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.net.SocketAddress;

public class ProxyPingListener implements Listener, Ping {

    private final SlimePlugin<Plugin> slimePlugin;

    public ProxyPingListener(SlimePlugin<Plugin> slimePlugin) {
        this.slimePlugin = slimePlugin;
    }

    @EventHandler
    public void onProxyPing(ProxyPingEvent event) {
        final ServerPing ping = event.getResponse();

        if (ping == null || event instanceof Cancellable && ((Cancellable) event).isCancelled()) {
            return;
        }

        final PendingConnection connection = event.getConnection();

        final SocketAddress address = connection.getSocketAddress();

        final int protocol = connection.getVersion();

        final String userName = getPlayerDatabase().getPlayer(
                address.toString()
        );

        //TODO: ProxyPingListener MOTD

    }

}
