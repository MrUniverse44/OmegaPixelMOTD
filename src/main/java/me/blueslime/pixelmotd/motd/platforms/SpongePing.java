package me.blueslime.pixelmotd.motd.platforms;

import me.blueslime.slimelib.colors.platforms.velocity.DefaultSlimeColor;
import me.blueslime.pixelmotd.motd.CachedMotd;
import me.blueslime.pixelmotd.motd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.PingBuilder;
import me.blueslime.pixelmotd.motd.builder.favicon.FaviconModule;
import me.blueslime.pixelmotd.motd.builder.hover.HoverModule;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.server.ClientPingServerEvent;
import org.spongepowered.api.network.status.Favicon;
import org.spongepowered.api.profile.GameProfile;

public class SpongePing extends PingBuilder<Server, Favicon, ClientPingServerEvent, GameProfile> {
    public SpongePing(
            PixelMOTD<Server> plugin,
            FaviconModule<Server, Favicon> faviconModule,
            HoverModule<GameProfile> hoverModule
    ) {
        super(plugin, faviconModule, hoverModule);
    }

    @Override
    public void execute(MotdType motdType, ClientPingServerEvent event, int code, String user) {
        ClientPingServerEvent.Response ping = event.response();

        CachedMotd motd = fetchMotd(motdType, code);

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
                    ping.setFavicon(favicon);
                }
            }
        }

        online = motd.getOnline(getPlugin());
        max    = motd.getMax(getPlugin(), online);

        if (motd.hasHover()) {
            if (ping.players().isPresent()) {
                ping.players().get().profiles().clear();

                ping.players().get().profiles().addAll(
                        getHoverModule().generate(
                                motd.getHover(),
                                user,
                                online,
                                max
                        )
                );
            }
        }

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

            result = Component.text(
                    completed
            );
        }

        ping.setDescription(result);

        if (ping.players().isPresent()) {
            ping.players().get().setOnline(online);
            ping.players().get().setMax(max);
        }
    }
}
