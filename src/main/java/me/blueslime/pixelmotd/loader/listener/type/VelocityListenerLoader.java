package me.blueslime.pixelmotd.loader.listener.type;

import com.velocitypowered.api.proxy.ProxyServer;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.velocity.VelocityListener;
import me.blueslime.pixelmotd.loader.listener.ListenerLoader;

public class VelocityListenerLoader extends ListenerLoader {
    @SuppressWarnings("unchecked")
    public VelocityListenerLoader(PixelMOTD<?> plugin) {
        for (VelocityListener listener : VelocityListener.values()) {
            try {
                ((PixelMOTD<ProxyServer>) plugin).addListener(
                        listener.getParent().getConstructor(PixelMOTD.class).newInstance(
                                plugin
                        )
                );
            } catch (Exception e) {
                plugin.getLogs().error("Can't register listener: " + listener + ", issue reported!", e);
            }
        }
    }
}
