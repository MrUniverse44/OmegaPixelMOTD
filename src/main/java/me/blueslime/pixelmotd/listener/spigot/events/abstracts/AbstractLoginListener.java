package me.blueslime.pixelmotd.listener.spigot.events.abstracts;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.ConnectionListener;
import me.blueslime.pixelmotd.utils.ListType;
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

        final String username = connection.getName();

        final UUID uuid = connection.getUniqueId();

        ConfigurationHandler settings = getControl();

        if (hasWhitelist()) {
            if (!checkPlayer(ListType.WHITELIST, "global", username) || !checkUUID(ListType.WHITELIST, "global", uuid)) {
                String reason = ListUtil.ListToString(settings.getStringList("kick-message.global-whitelist"));

                event.disallow(
                        PlayerLoginEvent.Result.KICK_WHITELIST,
                        colorize(
                                replace(
                                        reason,
                                        "whitelist.global",
                                        username,
                                        uuid.toString()
                                )
                        )
                );
                return;
            }
        }

        if (hasBlacklist()) {
            if (!checkPlayer(ListType.BLACKLIST, "global", username) || !checkUUID(ListType.BLACKLIST, "global", uuid)) {
                String reason = ListUtil.ListToString(settings.getStringList("kick-message.global-blacklist"));

                event.disallow(
                        PlayerLoginEvent.Result.KICK_WHITELIST,
                        colorize(
                                replace(
                                        reason,
                                        "blacklist.global",
                                        username,
                                        uuid.toString()
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
