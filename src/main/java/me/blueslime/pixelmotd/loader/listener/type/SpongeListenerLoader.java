package me.blueslime.pixelmotd.loader.listener.type;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.sponge.SpongeListener;
import me.blueslime.pixelmotd.loader.listener.ListenerLoader;
import org.spongepowered.api.Server;

public class SpongeListenerLoader extends ListenerLoader {
    @SuppressWarnings("unchecked")
    public SpongeListenerLoader(PixelMOTD<?> plugin) {
        for (SpongeListener listener : SpongeListener.values()) {
            try {
                ((PixelMOTD<Server>) plugin).addListener(
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
