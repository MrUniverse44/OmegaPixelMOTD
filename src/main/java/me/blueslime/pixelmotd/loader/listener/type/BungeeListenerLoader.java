package me.blueslime.pixelmotd.loader.listener.type;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.bungeecord.BungeeListener;
import me.blueslime.pixelmotd.loader.listener.ListenerLoader;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeListenerLoader extends ListenerLoader {
    @SuppressWarnings("unchecked")
    public BungeeListenerLoader(PixelMOTD<?> plugin) {
        for (BungeeListener listener : BungeeListener.values()) {
            try {
                ((PixelMOTD<Plugin>) plugin).addListener(
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
