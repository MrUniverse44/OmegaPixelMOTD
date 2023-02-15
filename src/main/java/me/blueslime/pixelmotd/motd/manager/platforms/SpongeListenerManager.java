package me.blueslime.pixelmotd.motd.manager.platforms;

import com.google.inject.Inject;
import me.blueslime.pixelmotd.motd.manager.ListenerManager;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.sponge.events.ServerPingListener;
import me.blueslime.pixelmotd.utils.Extras;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.plugin.PluginContainer;

@SuppressWarnings("unused")
public class SpongeListenerManager extends ListenerManager<Server> {

    private final ServerPingListener listener;

    @Inject
    private PluginContainer container;

    public SpongeListenerManager(PixelMOTD<Server> plugin) {
        super(plugin);

        this.listener = new ServerPingListener(plugin);
    }

    @Override
    public void register() {
        Sponge.eventManager().registerListeners(container, listener);

        getLogs().info("ServerPingListener has been registered to the server.");
    }

    @Override
    public void update() {
        listener.update();
    }

    @Override
    public boolean isPlayer() {
        return listener.getPingBuilder().isPlayerSystem();
    }

    @Override
    public Extras getExtras() {
        return listener.getPingBuilder().getExtras();
    }
}
