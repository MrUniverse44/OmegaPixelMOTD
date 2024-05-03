package me.blueslime.omegapixelmotd.modules.listeners.velocity.list;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.omegapixelmotd.modules.listeners.PluginListener;

public class ServerConnectListener extends PluginListener {
    public ServerConnectListener(OmegaPixelMOTD plugin) {
        super(plugin);
    }

    @Subscribe
    public void on(ServerPreConnectEvent event) {

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
