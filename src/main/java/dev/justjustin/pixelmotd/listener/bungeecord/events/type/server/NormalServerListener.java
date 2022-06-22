package dev.justjustin.pixelmotd.listener.bungeecord.events.type.server;

import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.listener.bungeecord.events.abstracts.AbstractServerConnectListener;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class NormalServerListener extends AbstractServerConnectListener implements Listener {

    public NormalServerListener(PixelMOTD<Plugin> plugin) {
        super(plugin);
    }

    @EventHandler
    public void onConnect(ServerConnectEvent event) {
        execute(event);
    }

}
