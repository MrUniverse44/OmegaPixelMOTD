package me.blueslime.pixelmotd.listener.sponge.player;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.type.SpongePluginListener;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.rcon.RconConnectionEvent;

public class LoginListener extends SpongePluginListener {
    public LoginListener(PixelMOTD<?> plugin) {
        super(plugin);
        register();
    }

    @Listener
    public void on(RconConnectionEvent event) {
        //TODO:
    }
}
