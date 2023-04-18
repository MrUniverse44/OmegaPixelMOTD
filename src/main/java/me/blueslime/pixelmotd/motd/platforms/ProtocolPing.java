package me.blueslime.pixelmotd.motd.platforms;

import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import me.blueslime.pixelmotd.utils.color.BungeeHexUtil;
import me.blueslime.slimelib.colors.platforms.StringSlimeColor;
import me.blueslime.pixelmotd.motd.CachedMotd;
import me.blueslime.pixelmotd.motd.MotdProtocol;
import me.blueslime.pixelmotd.motd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.external.iridiumcolorapi.IridiumColorAPI;
import me.blueslime.pixelmotd.motd.builder.PingBuilder;
import me.blueslime.pixelmotd.motd.builder.favicon.FaviconModule;
import me.blueslime.pixelmotd.motd.builder.hover.HoverModule;
import me.blueslime.pixelmotd.utils.PlaceholderParser;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class ProtocolPing extends PingBuilder<JavaPlugin, WrappedServerPing.CompressedImage, PacketEvent, WrappedGameProfile> {

    private final boolean hasPAPI;

    public ProtocolPing(
            PixelMOTD<JavaPlugin> plugin,
            FaviconModule<JavaPlugin, WrappedServerPing.CompressedImage> builder,
            HoverModule<WrappedGameProfile> hoverModule
    ) {
        super(plugin, builder, hoverModule);
        hasPAPI = plugin.getPlugin().getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    @Override
    public void execute(MotdType motdType, PacketEvent event, int code, String user) {
        WrappedServerPing ping = event.getPacket().getServerPings().read(0);

        if (ping == null) {
            if (isDebug()) {
                getLogs().debug("The ping was null in the index 0, searching in another index");
            }
            if (event.getPacket().getServerPings().size() > 2) {
                ping = event.getPacket().getServerPings().read(1);
                if (isDebug()) {
                    getLogs().debug("Currently reading index 1 of ping packet.");
                }
            }
        }

        if (ping == null) {
            if (isDebug()) {
                getLogs().debug("The plugin is receiving a null ping from ProtocolLib, please report it to ProtocolLib, this issue is not caused by PixelMOTD");
            }
            return;
        }

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
                if (isDebug()) {
                    getLogs().info("Icon applied");
                }
                WrappedServerPing.CompressedImage favicon = getFavicon().getFavicon(
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
            ping.setPlayers(
                    getHoverModule().generate(
                            motd.getHover(),
                            user,
                            online,
                            max
                    )
            );
        }

        MotdProtocol protocol = MotdProtocol.fromOther(
                motd.getModifier()
        ).setCode(code);

        if (protocol != MotdProtocol.DEFAULT) {
            if (protocol != MotdProtocol.ALWAYS_NEGATIVE) {
                ping.setVersionProtocol(code);
            } else {
                ping.setVersionProtocol(-1);
            }
        }

        ping.setVersionName(
                ChatColor.translateAlternateColorCodes(
                        '&',
                        getExtras().replace(
                                motd.getProtocolText(),
                                online,
                                max,
                                user
                        )
                )
        );

        if (!motd.hasHex()) {
            line1 = motd.getLine1();
            line2 = motd.getLine2();

            if (hasPAPI) {
                line1 = PlaceholderParser.parse(getPlugin().getLogs(), user, line1);
                line2 = PlaceholderParser.parse(getPlugin().getLogs(), user, line2);
            }

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

            completed = ChatColor.translateAlternateColorCodes('&', completed);

        } else {
            line1 = motd.getLine1();
            line2 = motd.getLine2();

            if (hasPAPI) {
                line1 = PlaceholderParser.parse(getPlugin().getLogs(), user, line1);
                line2 = PlaceholderParser.parse(getPlugin().getLogs(), user, line2);
            }

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

            if (completed.contains("<GRADIENT") || completed.contains("<RAINBOW") || completed.contains("<SOLID:")) {

                completed = IridiumColorAPI.process(completed);

            } else {
                completed = new StringSlimeColor(
                        completed,
                        true
                ).build();

                completed = BungeeHexUtil.convert(completed);
            }

            completed = ChatColor.translateAlternateColorCodes('&', completed);
        }

        ping.setMotD(
                WrappedChatComponent.fromLegacyText(completed)
        );


        ping.setPlayersOnline(online);
        ping.setPlayersMaximum(max);
    }
}
