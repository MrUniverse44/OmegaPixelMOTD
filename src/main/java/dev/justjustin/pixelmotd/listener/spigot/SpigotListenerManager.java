package dev.justjustin.pixelmotd.listener.spigot;

import dev.justjustin.pixelmotd.ListenerManager;
import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.listener.spigot.events.PacketServerPingListener;
import dev.justjustin.pixelmotd.listener.spigot.events.ServerPingListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotListenerManager implements ListenerManager {

    private final PixelMOTD<JavaPlugin> slimePlugin;

    @SuppressWarnings("unchecked")
    public <T> SpigotListenerManager(PixelMOTD<T> plugin) {
        this.slimePlugin = (PixelMOTD<JavaPlugin>) plugin;
    }

    @Override
    public void register() {
        final JavaPlugin plugin = slimePlugin.getPlugin();

        PluginManager manager = plugin.getServer().getPluginManager();

        if (manager.isPluginEnabled("ProtocolLib")) {
            new PacketServerPingListener(slimePlugin).register();
            slimePlugin.getLogs().info("Using ProtocolLib for motds, enabling all features...");
        } else {
            manager.registerEvents(new ServerPingListener(slimePlugin), plugin);
            slimePlugin.getLogs().info("Using default motd system from minecraft, disabling some features..");
        }
    }

}
