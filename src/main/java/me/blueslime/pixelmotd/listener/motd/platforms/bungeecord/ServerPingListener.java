package me.blueslime.pixelmotd.listener.motd.platforms.bungeecord;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.motd.MotdListener;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class ServerPingListener extends MotdListener<Plugin> implements Listener {
    public ServerPingListener(PixelMOTD<Plugin> plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPing(ProxyPingEvent event) {

    }

    @Override
    public void register() {
        getPlugin().getProxy().getPluginManager().registerListener(getPlugin(), this);
    }
}
