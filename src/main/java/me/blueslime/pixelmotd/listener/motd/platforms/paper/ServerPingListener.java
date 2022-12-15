package me.blueslime.pixelmotd.listener.motd.platforms.paper;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.motd.MotdListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerPingListener extends MotdListener<JavaPlugin> implements Listener {

    public ServerPingListener(PixelMOTD<JavaPlugin> plugin) {
        super(plugin);
    }



    @Override
    public void register() {
        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    }
}
