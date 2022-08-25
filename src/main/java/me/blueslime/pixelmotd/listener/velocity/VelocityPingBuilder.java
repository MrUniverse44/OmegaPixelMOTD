package me.blueslime.pixelmotd.listener.velocity;

import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import me.blueslime.pixelmotd.MotdProtocol;
import me.blueslime.pixelmotd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.MotdBuilder;
import me.blueslime.pixelmotd.listener.PingBuilder;
import me.blueslime.pixelmotd.utils.MotdPlayers;
import dev.mruniverse.slimelib.colors.platforms.velocity.DefaultSlimeColor;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
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
                Favicon img = getBuilder().getFavicon(
                        motdType,
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
            MotdProtocol protocol = MotdProtocol.fromString(
                    control.getString(path + "protocol.modifier", "ALWAYS_POSITIVE")
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

            switch (protocol) {
                case DEFAULT:
                    if (isDebug()) {
                        logs.debug("The plugin will not modify the protocol because was detected in DEFAULT Modifier, client-protocol: " + code);
                    }
                    break;
                case ALWAYS_NEGATIVE:
                    if (isDebug()) {
                        logs.debug("The plugin will modify the protocol because was detected in NEGATIVE Modifier, client-protocol: " + code);
                    }
                    p1 = protocol.getCode();
                    break;
                case ALWAYS_POSITIVE:
                    if (isDebug()) {
                        logs.debug("The plugin will modify the protocol because was detected in POSITIVE Modifier, client-protocol: " + code);
                    }
                    p1 = code;
                    break;
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

    @Override
    public ServerPing.SamplePlayer[] getHover(MotdType motdType, String path, int online, int max, String user) {
        ConfigurationHandler control = getPlugin().getConfigurationHandler(motdType.getFile());

        ServerPing.SamplePlayer[] hoverToShow = new ServerPing.SamplePlayer[0];

        List<String> lines;

        if (isPlayerSystem()) {
            lines = getExtras().replaceHoverLine(
                    control.getStringList(path + "hover.lines")
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
