package me.blueslime.pixelmotd.motd.platforms;

import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import dev.mruniverse.slimelib.colors.platforms.velocity.DefaultSlimeColor;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.motd.MotdProtocol;
import me.blueslime.pixelmotd.motd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.PingBuilder;
import me.blueslime.pixelmotd.motd.builder.favicon.FaviconModule;
import me.blueslime.pixelmotd.motd.builder.hover.HoverModule;
import me.blueslime.pixelmotd.utils.MotdPlayers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class VelocityPing extends PingBuilder<ProxyServer, Favicon, ProxyPingEvent, ServerPing.SamplePlayer> {

    public VelocityPing(
            PixelMOTD<ProxyServer> plugin,
            FaviconModule<ProxyServer, Favicon> builder,
            HoverModule<ServerPing.SamplePlayer> hoverModule
    ) {
        super(plugin, builder, hoverModule);
    }

    @Override
    public void execute(MotdType motdType, ProxyPingEvent event, int code, String user) {
        final SlimeLogs logs = getPlugin().getLogs();

        final ConfigurationHandler control = getPlugin().getConfigurationHandler(motdType.getFile());

        ServerPing.Builder ping = event.getPing().asBuilder();

        String motd = getMotd(motdType);

        if (motd.equals("8293829382382732127413475y42732749832748327472fyfs")) {
            if (isDebug()) {
                logs.debug("The plugin don't detect motds for MotdType: " + motdType);
            }
            return;
        }

        String path = motdType + "." + motd + ".";

        String line1, line2, completed;

        int online, max;

        if (isIconSystem()) {
            String iconName = control.getString(
                    path + "icons.icon"
            );

            if (!iconName.equalsIgnoreCase("") && !iconName.equalsIgnoreCase("disabled")) {
                Favicon img = getFavicon().getFavicon(
                        iconName
                );
                if (img != null) {
                    ping.favicon(img);
                }
            }
        }

        if (control.getStatus(path + "players.online.toggle", false)) {
            online = MotdPlayers.getModeFromText(
                    control,
                    control.getString(path + "players.online.type", "add"),
                    getPlugin().getPlayerHandler().getPlayersSize(),
                    path + "players.online."
            );
        } else {
            online = ping.getOnlinePlayers();
        }
        if (control.getStatus(path + "players.max.toggle", false)) {
            String mode = control.getString(path + "players.max.type", "add").toLowerCase();
            if (mode.contains("equal")) {
                max = MotdPlayers.getModeFromText(
                        control,
                        mode,
                        getPlugin().getPlugin().getPlayerCount(),
                        path + "players.max."
                );
            } else {
                max = MotdPlayers.getModeFromText(
                        control,
                        mode,
                        online,
                        path + "players.max."
                );
            }
        } else {
            max = ping.getMaximumPlayers();
        }

        if (control.getStatus(path + "hover.toggle", false)) {
            ping.clearSamplePlayers();

            ServerPing.SamplePlayer[] array = getHoverModule().convert(
                    getHoverModule().generate(
                            control,
                            path,
                            user,
                            online,
                            max
                    )
            );

            ping.samplePlayers(
                    array
            );
        }

        if (control.getStatus(path + "protocol.toggle")) {
            MotdProtocol protocol = MotdProtocol.getFromText(
                    control.getString(control.getString(path + "protocol.modifier", "ALWAYS_POSITIVE")),
                    code
            );

            int p1 = ping.getVersion().getProtocol();

            Component n1 = new DefaultSlimeColor(
                    getExtras().replace(
                            control.getString(path + "protocol.message", "PixelMOTD System"),
                            online,
                            max,
                            user
                    ),
                    true
            ).build();

            if (protocol != MotdProtocol.DEFAULT) {
                p1 = protocol.getCode();
            }

            ping.version(
                    new ServerPing.Version(
                            p1,
                            legacy(n1)
                    )
            );
        }

        Component result;

        if (motdType.isHexMotd()) {

            line1 = control.getString(path + "line1", "");

            line2 = control.getString(path + "line2", "");

            completed = getExtras().replace(
                    line1,
                    online,
                    max,
                    user
            ) + "\n" + getExtras().replace(
                    line2,
                    online,
                    max,
                    user
            );

            result = new DefaultSlimeColor(completed, true)
                    .build();

        } else {

            line1 = legacy(
                    control.getString(path + "line1", "")
            );

            line2 = legacy(
                    control.getString(path + "line2", "")
            );

            completed = getExtras().replace(
                    line1,
                    online,
                    max,
                    user
            ) + "\n" + getExtras().replace(
                    line2,
                    online,
                    max,
                    user
            );

            result = color(completed);
        }

        ping.description(result);
        ping.onlinePlayers(online);
        ping.maximumPlayers(max);

        event.setPing(ping.build());
    }

    private TextComponent color(String text) {
        return LegacyComponentSerializer.legacySection().deserialize(text);
    }

    private @NotNull String legacy(String content) {
        Component color = new DefaultSlimeColor(content, true)
                .build();

        return LegacyComponentSerializer.legacySection().serialize(
                color
        );
    }

    private @NotNull String legacy(Component color) {
        return LegacyComponentSerializer.legacySection().serialize(
                color
        );
    }
}