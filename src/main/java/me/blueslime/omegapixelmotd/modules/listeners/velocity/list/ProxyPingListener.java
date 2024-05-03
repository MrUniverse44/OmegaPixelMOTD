package me.blueslime.omegapixelmotd.modules.listeners.velocity.list;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.omegapixelmotd.modules.listeners.PluginListener;

public class ProxyPingListener extends PluginListener {
    public ProxyPingListener(OmegaPixelMOTD plugin) {
        super(plugin);
    }

    @Subscribe
    public void on(ProxyPingEvent event) {

    }

    @Override
    public void initialize() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void reload() {

    }
}
