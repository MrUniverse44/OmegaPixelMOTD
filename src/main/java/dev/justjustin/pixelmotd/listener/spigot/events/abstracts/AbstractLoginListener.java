package dev.justjustin.pixelmotd.listener.spigot.events.abstracts;

import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.listener.ConnectionListener;
import dev.justjustin.pixelmotd.utils.ListUtil;
import dev.mruniverse.slimelib.control.Control;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public abstract class AbstractLoginListener  extends ConnectionListener<JavaPlugin, PlayerLoginEvent, String> implements Listener, EventExecutor {

    public AbstractLoginListener(PixelMOTD<JavaPlugin> plugin) {
        super(plugin);
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

        final String username = connection.getName();

        final String uuid = connection.getUniqueId().toString();

        ConfigurationHandler settings = getControl();

        String path = ".global.players.by-";

        if (hasWhitelist()) {
            if (settings.getStringList("whitelist" + path + "name").contains(username) ||
                    !settings.getStringList("whitelist" + path + "uuid").contains(uuid)
            ) {
                String reason = ListUtil.ListToString(settings.getStringList("kick-message.global-whitelist"));

                event.disallow(
                        PlayerLoginEvent.Result.KICK_WHITELIST,
                        colorize(
                                replace(
                                        reason,
                                        "whitelist.global",
                                        username,
                                        uuid
                                )
                        )
                );
                return;
            }
        }

        if (hasBlacklist()) {
            if (settings.getStringList("blacklist" + path + "name").contains(username) ||
                    !settings.getStringList("blacklist" + path + "uuid").contains(uuid)
            ) {
                String reason = ListUtil.ListToString(settings.getStringList("kick-message.global-blacklist"));

                event.disallow(
                        PlayerLoginEvent.Result.KICK_WHITELIST,
                        colorize(
                                replace(
                                        reason,
                                        "blacklist.global",
                                        username,
                                        uuid
                                )
                        )
                );
            }
        }



    }

    @Override
    public String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
