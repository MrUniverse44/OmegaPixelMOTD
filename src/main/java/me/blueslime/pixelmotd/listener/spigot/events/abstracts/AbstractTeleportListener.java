package me.blueslime.pixelmotd.listener.spigot.events.abstracts;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.ConnectionListener;
import me.blueslime.pixelmotd.utils.ListType;
import me.blueslime.pixelmotd.utils.ListUtil;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public abstract class AbstractTeleportListener extends ConnectionListener<JavaPlugin, PlayerTeleportEvent, String> implements Listener, EventExecutor {
    public AbstractTeleportListener(PixelMOTD<JavaPlugin> plugin) {
        super(plugin);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void execute(PlayerTeleportEvent event) {
        if (event == null || event.isCancelled()) {
            return;
        }

        final Player connection = event.getPlayer();

        final String username = connection.getName();

        final UUID uuid = connection.getUniqueId();

        final String playerWorld = connection.getWorld().getName();

        if (event.getTo() == null || event.getTo().getWorld() == null) {
            return;
        }

        final String target = event.getTo().getWorld().getName();

        if (playerWorld.equalsIgnoreCase(target)) {
            return;
        }

        ConfigurationHandler whitelist = getWhitelist();
        ConfigurationHandler blacklist = getBlacklist();

        String path = getPlace().loweredName() + "." + target;

        if (whitelist.getStatus(path + ".enabled", false)) {
            if (!checkPlayer(ListType.WHITELIST, path, username) && !checkUUID(ListType.WHITELIST, path, uuid)) {
                String reason = ListUtil.ListToString(whitelist.getStringList("kick-message.individual"));

                connection.sendMessage(
                        colorize(
                                replace(
                                        reason,
                                        true,
                                        target,
                                        username,
                                        uuid.toString()
                                )
                        )
                );

                event.setCancelled(true);
                return;
            }
        }

        if (blacklist.getStatus(path + ".enabled", false)) {
            if (checkPlayer(ListType.BLACKLIST, path, username) || checkUUID(ListType.BLACKLIST, path, uuid)) {
                String reason = ListUtil.ListToString(blacklist.getStringList("kick-message.individual"));

                connection.sendMessage(
                        colorize(
                                replace(
                                        reason,
                                        false,
                                        target,
                                        username,
                                        uuid.toString()
                                )
                        )
                );

                event.setCancelled(true);
            }
        }
    }

    @Override
    public String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
