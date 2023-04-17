package me.blueslime.pixelmotd.listener.velocity.player;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.type.VelocityPluginListener;
import me.blueslime.pixelmotd.utils.ListType;
import me.blueslime.pixelmotd.utils.ListUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.UUID;

public class LoginListener extends VelocityPluginListener {
    public LoginListener(PixelMOTD<?> plugin) {
        super(plugin);
        register();
    }

    @Subscribe(order = PostOrder.EARLY)
    public void on(LoginEvent event) {
        if (event == null) {
            return;
        }

        final Player connection = event.getPlayer();

        if (connection == null) {
            return;
        }

        if (connection.getUniqueId() == null) {

            getLogs().info("The plugin will not apply the whitelist or blacklist check for a null-ip and will block player from joining to the server");



            event.setResult(
                    result("&cBlocking you from joining to the server, reason: &6Null IP&c, if this is a error, contact to the server staff")
            );
            return;

        }

        final InetSocketAddress socketAddress = connection.getRemoteAddress();

        if (socketAddress != null) {
            InetAddress address = socketAddress.getAddress();

            getPlayerDatabase().add(
                    address.getHostAddress(),
                    connection.getUsername()
            );
        }

        final String username = connection.getUsername();

        final UUID uuid = connection.getUniqueId();

        ConfigurationHandler whitelist = getWhitelist();
        ConfigurationHandler blacklist = getBlacklist();

        if (getWhitelist().getBoolean("enabled", false)) {
            if (!checkPlayer(ListType.WHITELIST, "global", username) && !checkUUID(ListType.WHITELIST, "global", uuid)) {
                String reason = ListUtil.ListToString(whitelist.getStringList("kick-message.global"));

                event.setResult(
                        result(
                                replace(
                                        reason,
                                        true,
                                        "global",
                                        username,
                                        uuid.toString()
                                )
                        )
                );
                return;
            }
        }

        if (getBlacklist().getBoolean("enabled", false)) {
            if (checkPlayer(ListType.BLACKLIST, "global", username) || checkUUID(ListType.BLACKLIST, "global", uuid)) {
                String reason = ListUtil.ListToString(blacklist.getStringList("kick-message.global"));

                event.setResult(
                        result(
                                replace(
                                        reason,
                                        false,
                                        "global",
                                        username,
                                        uuid.toString()
                                )
                        )
                );
            }
        }
    }

    private ResultedEvent.ComponentResult result(String message) {
        return ResultedEvent.ComponentResult.denied(
                colorize(message)
        );
    }

    public Component colorize(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }
}
