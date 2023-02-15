package me.blueslime.pixelmotd.motd.platforms;

import dev.mruniverse.slimelib.colors.platforms.velocity.DefaultSlimeColor;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import me.blueslime.pixelmotd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.PingBuilder;
import me.blueslime.pixelmotd.motd.builder.favicon.FaviconModule;
import me.blueslime.pixelmotd.motd.builder.hover.HoverModule;
import me.blueslime.pixelmotd.utils.MotdPlayers;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.server.ClientPingServerEvent;
import org.spongepowered.api.network.status.Favicon;
import org.spongepowered.api.network.status.StatusResponse;
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
        final ConfigurationHandler control = getPlugin().getConfigurationHandler(motdType.getFile());

        ClientPingServerEvent.Response ping = event.response();

        String motd = getMotd(motdType);

        if (motd.equals("8293829382382732127413475y42732749832748327472fyfs")) {
            if (isDebug()) {
                getLogs().debug("The plugin don't detect motds for MotdType: " + motdType);
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
                    ping.setFavicon(img);
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
            online = ping.players().map(StatusResponse.Players::online)
                    .orElseGet(() -> getPlugin().getPlayerHandler().getPlayersSize());
        }
        if (control.getStatus(path + "players.max.toggle", false)) {
            String mode = control.getString(path + "players.max.type", "add").toLowerCase();
            if (mode.contains("equal")) {
                max = MotdPlayers.getModeFromText(
                        control,
                        mode,
                        getPlugin().getPlayerHandler().getPlayersSize(),
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
            max = ping.players().map(StatusResponse.Players::max)
                    .orElseGet(() -> getPlugin().getPlayerHandler().getMaxPlayers());
        }

        if (control.getStatus(path + "hover.toggle", false)) {
            if (ping.players().isPresent()) {
                ping.players().get().profiles().clear();

                ping.players().get().profiles().addAll(
                        getHoverModule().generate(
                                control,
                                path,
                                user,
                                online,
                                max
                        )
                );
            }
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
