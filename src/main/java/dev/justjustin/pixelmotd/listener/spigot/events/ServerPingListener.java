package dev.justjustin.pixelmotd.listener.spigot.events;

import dev.mruniverse.slimelib.SlimePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerPingListener implements Listener {

    private final SlimePlugin<JavaPlugin> slimePlugin;

    public ServerPingListener(SlimePlugin<JavaPlugin> slimePlugin) {
        this.slimePlugin = slimePlugin;
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {

    }

}
