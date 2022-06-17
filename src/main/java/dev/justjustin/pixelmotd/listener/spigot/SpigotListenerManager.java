package dev.justjustin.pixelmotd.listener.spigot;

import dev.justjustin.pixelmotd.ListenerManager;
import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.listener.spigot.events.PacketServerPingListener;
import dev.justjustin.pixelmotd.listener.spigot.events.ServerPingListener;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotListenerManager implements ListenerManager {

    private final PixelMOTD<JavaPlugin> slimePlugin;

    private PacketServerPingListener packetListener;
    private ServerPingListener serverListener;

    private final SlimeLogs logs;

    private boolean isPacket = false;

    @SuppressWarnings("unchecked")
    public <T> SpigotListenerManager(PixelMOTD<T> plugin, SlimeLogs logs) {
        this.slimePlugin = (PixelMOTD<JavaPlugin>) plugin;
        this.logs = logs;
    }

    @Override
    public void register() {
        final JavaPlugin plugin = slimePlugin.getPlugin();

        PluginManager manager = plugin.getServer().getPluginManager();

        if (manager.isPluginEnabled("ProtocolLib")) {
            isPacket = true;

            packetListener = new PacketServerPingListener(slimePlugin);
            packetListener.register();

            logs.info("Using ProtocolLib for motds, enabling all features...");
        } else {
            isPacket = false;

            serverListener = new ServerPingListener(slimePlugin);

            manager.registerEvents(serverListener, plugin);
            logs.info("Using default motd system from minecraft, disabling some features..");
        }
    }

    @Override
    public void update() {
        if (isPacket) {
            packetListener.update();
        } else {
            serverListener.update();
        }
    }

}
