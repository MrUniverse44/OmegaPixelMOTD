package me.blueslime.pixelmotd.listener.motd.platforms.bukkit;

import me.blueslime.pixelmotd.PixelMOTD;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import me.blueslime.pixelmotd.listener.motd.MotdListener;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerPingListener extends MotdListener<JavaPlugin> implements Listener {

    public ServerPingListener(PixelMOTD<JavaPlugin> plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void setMotd(ServerListPingEvent event) {

    }

    @Override
    public void register() {
        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    }
}
