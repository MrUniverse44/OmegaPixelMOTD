package dev.justjustin.pixelmotd.listener.velocity;

import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import dev.justjustin.pixelmotd.MotdProtocol;
import dev.justjustin.pixelmotd.MotdType;
import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.listener.MotdBuilder;
import dev.justjustin.pixelmotd.listener.PingBuilder;
import dev.justjustin.pixelmotd.utils.MotdPlayers;
import dev.mruniverse.slimelib.colors.platforms.velocity.DefaultSlimeColor;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.file.configuration.TextDecoration;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class VelocityPingBuilder extends PingBuilder<ProxyServer, Favicon, ProxyPingEvent, ServerPing.SamplePlayer> {

    public VelocityPingBuilder(PixelMOTD<ProxyServer> plugin, MotdBuilder<ProxyServer, Favicon> builder) {
        super(plugin, builder);
    }

    @Override
    public void execute(MotdType motdType, ProxyPingEvent event, int code, String user) {
        final SlimeLogs logs = getPlugin().getLogs();

        final ConfigurationHandler control = getPlugin().getConfigurationHandler(motdType.getFile());

        ServerPing.Builder ping = event.getPing().asBuilder();

        String motd;

        try {
            motd = getMotd(motdType);
        } catch (Exception ignored) {
            logs.error("This file isn't updated to the latest file or the motd-path is incorrect, can't find motds for MotdType: " + motdType);
            return;
        }

        String path = motdType + "." + motd + ".";

        String line1, line2, completed;

        int online, max;

        if (isIconSystem()) {
            Favicon img = getBuilder().getFavicon(
                    motdType,
                    control.getString(
                            path + "icons.icon"
                    )
            );
            if (img != null) {
                ping.favicon(img);
            }
        }

        if (control.getStatus(path + "players.online.toggle")) {
            online = MotdPlayers.getModeFromText(
                    control,
                    control.getString(path + "players.online.type", ""),
                    getPlugin().getPlayerHandler().getPlayersSize(),
                    path + "players.online."
            );
        } else {
            online = ping.getOnlinePlayers();
        }
        if (control.getStatus(path + "players.max.toggle")) {
            String mode = control.getString(path + "players.max.type", "").toLowerCase();
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

        if (control.getStatus(path + "hover.toggle")) {
            ping.samplePlayers(
                    getHover(
                            motdType,
                            path,
                            online,
                            max,
                            user
                    )
            );
        }

        if (control.getStatus(path + "protocol.toggle")) {
            MotdProtocol protocol = MotdProtocol.getFromText(
                    control.getString(control.getString(path + "protocol.modifier", "")),
                    code
            );

            int p1 = ping.getVersion().getProtocol();

            Component n1 = new DefaultSlimeColor(
                    getExtras().replace(
                            control.getString(path + "protocol.message"),
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

            line1 = control.getString(TextDecoration.LEGACY, path + "line1", "");
            line2 = control.getString(TextDecoration.LEGACY, path + "line2", "");

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

    @Override
    public ServerPing.SamplePlayer[] getHover(MotdType motdType, String path, int online, int max, String user) {
        ConfigurationHandler control = getPlugin().getConfigurationHandler(motdType.getFile());

        ServerPing.SamplePlayer[] hoverToShow = new ServerPing.SamplePlayer[0];

        List<String> lines;

        if (isPlayerSystem()) {
            lines = getExtras().replaceHoverLine(
                    control.getStringList(path + "hover.lines"),
                    control.getInt(path + "hover.hasMoreOnline")
            );
        } else {
            lines = control.getStringList(path + "hover.lines");
        }

        final UUID uuid = UUID.fromString("0-0-0-0-0");

        for (String line : lines) {
            hoverToShow = addHoverLine(
                    hoverToShow,
                    new ServerPing.SamplePlayer(
                            legacy(
                                    getExtras().replace(
                                            line,
                                            online,
                                            max,
                                            user
                                    )
                            ),
                            uuid
                    )
            );
        }

        return hoverToShow;
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

    @Override
    public ServerPing.SamplePlayer[] addHoverLine(ServerPing.SamplePlayer[] player, ServerPing.SamplePlayer info) {
        ServerPing.SamplePlayer[] samplePlayers = new ServerPing.SamplePlayer[player.length + 1];
        System.arraycopy(player, 0, samplePlayers, 0, player.length);
        samplePlayers[player.length] = info;
        return samplePlayers;
    }
}
