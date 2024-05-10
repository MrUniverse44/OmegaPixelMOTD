package me.blueslime.omegapixelmotd.modules.listeners.bukkit.list;

import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.omegapixelmotd.modules.listeners.ping.BukkitPingListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerPingListener extends BukkitPingListener implements Listener {
    public ServerPingListener(OmegaPixelMOTD plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        super.initialize();

        JavaPlugin plugin = getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void on(ServerListPingEvent event) {
        //TODO: Work in progress
    }
}
