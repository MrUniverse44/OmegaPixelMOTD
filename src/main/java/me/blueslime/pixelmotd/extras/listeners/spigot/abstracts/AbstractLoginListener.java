package me.blueslime.pixelmotd.extras.listeners.spigot.abstracts;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.extras.listeners.ConnectionListener;
import me.blueslime.pixelmotd.utils.ListUtil;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.UUID;

public abstract class AbstractLoginListener  extends ConnectionListener<JavaPlugin, PlayerLoginEvent, String> implements Listener, EventExecutor {

    public AbstractLoginListener(PixelMOTD<JavaPlugin> plugin) {
        super(plugin);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void execute(PlayerLoginEvent event) {
        if (event == null) {
            return;
        }

        final Player connection = event.getPlayer();

        final InetSocketAddress socketAddress = connection.getAddress();

        if (socketAddress != null) {
            InetAddress address = socketAddress.getAddress();

            getPlayerDatabase().add(
                    address.getHostAddress(),
                    connection.getName()
            );
        }

    }

    @Override
    public String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
