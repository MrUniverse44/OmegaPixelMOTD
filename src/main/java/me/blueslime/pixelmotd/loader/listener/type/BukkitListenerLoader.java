package me.blueslime.pixelmotd.loader.listener.type;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.bukkit.BukkitListener;
import me.blueslime.pixelmotd.loader.listener.ListenerLoader;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitListenerLoader extends ListenerLoader {
    @SuppressWarnings("unchecked")
    public BukkitListenerLoader(PixelMOTD<?> plugin) {
        PixelMOTD<JavaPlugin> original = (PixelMOTD<JavaPlugin>) plugin;

        boolean hasProtocol = original.getPlugin().getServer().getPluginManager().isPluginEnabled("ProtocolLib");

        for (BukkitListener listener : BukkitListener.values()) {
            if (listener == BukkitListener.SERVER_LIST_PING && hasProtocol) {
                continue;
            }
            if (listener == BukkitListener.PACKET_LISTENER && !hasProtocol) {
                continue;
            }
            try {
                ((PixelMOTD<JavaPlugin>) plugin).addListener(
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
