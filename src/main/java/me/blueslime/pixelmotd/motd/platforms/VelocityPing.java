package me.blueslime.pixelmotd.motd.platforms;

import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import dev.mruniverse.slimelib.colors.platforms.velocity.DefaultSlimeColor;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.motd.CachedMotd;
import me.blueslime.pixelmotd.motd.MotdProtocol;
import me.blueslime.pixelmotd.motd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.PingBuilder;
import me.blueslime.pixelmotd.motd.builder.favicon.FaviconModule;
import me.blueslime.pixelmotd.motd.builder.hover.HoverModule;
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
        ServerPing.Builder ping = event.getPing().asBuilder();

        CachedMotd motd = getMotd(motdType);

        if (motd == null) {
            if (isDebug()) {
                getLogs().debug("The plugin don't detect motds for MotdType: " + motdType);
            }
            return;
        }

        String line1, line2, completed;

        int online, max;

        if (isIconSystem()) {
            if (motd.hasServerIcon()) {
                Favicon favicon = getFavicon().getFavicon(
                        motd.getServerIcon()
                );
                if (favicon != null) {
                    ping.favicon(favicon);
                }
            }
        }

        online = motd.getOnline(getPlugin());
        max    = motd.getMax(getPlugin(), online);

        if (motd.hasHover()) {
            ping.clearSamplePlayers();

            ServerPing.SamplePlayer[] array = getHoverModule().convert(
                    getHoverModule().generate(
                            motd.getHover(),
                            user,
                            online,
                            max
                    )
            );

            ping.samplePlayers(
                    array
            );
        }

        MotdProtocol protocol = MotdProtocol.fromOther(
                motd.getModifier()
        );

        if (protocol != MotdProtocol.ALWAYS_NEGATIVE) {
            protocol = protocol.setCode(code);
        }

        int p1 = ping.getVersion().getProtocol();

        Component n1 = new DefaultSlimeColor(
                getExtras().replace(
                        motd.getProtocolText(),
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

        Component result;

        if (motd.hasHex()) {
            line1 = motd.getLine1();
            line2 = motd.getLine2();

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
                    motd.getLine1()
            );
            line2 = legacy(
                    motd.getLine2()
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