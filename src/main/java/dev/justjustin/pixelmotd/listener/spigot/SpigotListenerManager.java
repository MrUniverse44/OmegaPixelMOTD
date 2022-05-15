package dev.justjustin.pixelmotd.listener.spigot;

import dev.justjustin.pixelmotd.ListenerManager;
import dev.justjustin.pixelmotd.listener.spigot.events.PacketServerPingListener;
import dev.justjustin.pixelmotd.listener.spigot.events.ServerPingListener;
import dev.mruniverse.slimelib.SlimePlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotListenerManager implements ListenerManager {

    private final SlimePlugin<JavaPlugin> slimePlugin;

    @SuppressWarnings("unchecked")
    public <T> SpigotListenerManager(SlimePlugin<T> plugin) {
        this.slimePlugin = (SlimePlugin<JavaPlugin>) plugin;
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
