package me.blueslime.pixelmotd.listener.spigot.events.abstracts;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.ConnectionListener;
import me.blueslime.pixelmotd.utils.ListUtil;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractTeleportListener extends ConnectionListener<JavaPlugin, PlayerTeleportEvent, String> implements Listener, EventExecutor {
    public AbstractTeleportListener(PixelMOTD<JavaPlugin> plugin) {
        super(plugin);
    }

    @Override
    public void execute(PlayerTeleportEvent event) {
        if (event == null || event.isCancelled()) {
            return;
        }

        final Player connection = event.getPlayer();

        final String username = connection.getName();

        final String uuid = connection.getUniqueId().toString();

        final String playerWorld = connection.getWorld().getName();

        if (event.getTo() == null || event.getTo().getWorld() == null) {
            return;
        }

        final String target = event.getTo().getWorld().getName();

        if (playerWorld.equalsIgnoreCase(target)) {
            return;
        }

        ConfigurationHandler settings = getControl();

        String path = "." + getPlace().toStringLowerCase() + "." + target + ".players.by-";

        if (settings.getStatus("whitelist" + target + ".enabled", false)) {
            if (!settings.getStringList("whitelist" + path + "name").contains(username) ||
                    !settings.getStringList("whitelist" + path + "uuid").contains(uuid)
            ) {
                String reason = ListUtil.ListToString(settings.getStringList("kick-message.whitelist"));

                connection.sendMessage(
                        colorize(
                                replace(
                                        reason,
                                        "whitelist." + target,
                                        username,
                                        uuid
                                )
                        )
                );

                event.setCancelled(true);
                return;
            }
        }

        if (hasBlacklist()) {
            if (settings.getStringList("blacklist" + path + "name").contains(username) ||
                    settings.getStringList("blacklist" + path + "uuid").contains(uuid)
            ) {
                String reason = ListUtil.ListToString(settings.getStringList("kick-message.blacklist"));

                connection.sendMessage(
                        colorize(
                                replace(
                                        reason,
                                        "blacklist." + target,
                                        username,
                                        uuid
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
