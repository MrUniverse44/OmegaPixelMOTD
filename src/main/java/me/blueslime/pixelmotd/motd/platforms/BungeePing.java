package me.blueslime.pixelmotd.motd.platforms;

import dev.mruniverse.slimelib.colors.platforms.bungeecord.BungeeSlimeColor;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.file.configuration.TextDecoration;
import dev.mruniverse.slimelib.utils.ClassUtils;
import me.blueslime.pixelmotd.motd.MotdProtocol;
import me.blueslime.pixelmotd.motd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.external.minedown.MineDown;
import me.blueslime.pixelmotd.motd.builder.PingBuilder;
import me.blueslime.pixelmotd.motd.builder.favicon.FaviconModule;
import me.blueslime.pixelmotd.motd.builder.hover.HoverModule;
import me.blueslime.pixelmotd.utils.MotdPlayers;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeePing extends PingBuilder<Plugin, Favicon, ServerPing, ServerPing.PlayerInfo> {
    private static final boolean HAS_RGB_SUPPORT = ClassUtils.hasMethod(ChatColor.class, "of", String.class);

    public BungeePing(
            PixelMOTD<Plugin> plugin,
            FaviconModule<Plugin, Favicon> builder,
            HoverModule<ServerPing.PlayerInfo> hoverModule
    ) {
        super(plugin, builder, hoverModule);
    }

    @Override
    public void execute(MotdType motdType, ServerPing ping, int code, String user) {
        final ConfigurationHandler control = getPlugin().getConfigurationHandler(motdType.getFile());

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
                        control.getString(
                                path + "icons.icon"
                        )
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
            online = ping.getPlayers().getOnline();
        }
        if (control.getStatus(path + "players.max.toggle", false)) {
            String mode = control.getString(path + "players.max.type", "add").toLowerCase();
            if (mode.contains("equal")) {
                max = MotdPlayers.getModeFromText(
                        control,
                        mode,
                        ping.getPlayers().getMax(),
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
            max = ping.getPlayers().getMax();
        }

        if (control.getStatus(path + "hover.toggle", false)) {
            ServerPing.PlayerInfo[] array = getHoverModule().convert(
                    getHoverModule().generate(
                            control,
                            user,
                            path,
                            online,
                            max
                    )
            );

            ping.getPlayers().setSample(
                    array
            );
        }

        if (control.getStatus(path + "protocol.toggle", true)) {
            MotdProtocol protocol = MotdProtocol.getFromText(
                    control.getString(control.getString(path + "protocol.modifier", "ALWAYS_POSITIVE")),
                    code
            );

            if (protocol != MotdProtocol.DEFAULT) {
                ping.getVersion().setProtocol(protocol.getCode());
            }

            ping.getVersion().setName(
                    ChatColor.translateAlternateColorCodes(
                            '&',
                            getExtras().replace(
                                    control.getString(path + "protocol.message", "PixelMOTD System"),
                                    online,
                                    max,
                                    user
                            )
                    )
            );
        }

        TextComponent result = new TextComponent("");

        if (motdType.isHexMotd()) {

            line1 = control.getString(path + "line1", "");
            line2 = control.getString(path + "line2", "");

            completed = getExtras().replace(line1, online, max, user) + "\n" + getExtras().replace(line2, online, max, user);

            if (line1.contains("%(slimecolor") || line2.contains("%(slimecolor")) {

                if (isDebug()) {
                    getLogs().debug("Using SlimeColorAPI for the motd lines:" + completed);
                }

                result.addExtra(
                        new BungeeSlimeColor(completed, HAS_RGB_SUPPORT)
                                .build()
                );

            }  else {
                if (isDebug()) {
                    getLogs().debug("Using MineDown for the motd lines:" + completed);
                }
                result = new TextComponent(
                        new MineDown(
                                completed.replace('§', '&')).urlDetection(false).toComponent()
                );

            }

        } else {

            line1 = control.getString(TextDecoration.LEGACY, path + "line1", "");
            line2 = control.getString(TextDecoration.LEGACY, path + "line2", "");

            completed = getExtras().replace(line1, online, max, user) + "\n" + getExtras().replace(line2, online, max, user);

            if (completed.contains("<#") && completed.contains(">") && isDebug()) {
                getLogs().info("Are you trying to use gradients in a MotdType without support to gradients? :(, please remove <# or > from your motd lines");
                getLogs().info("to stop this spam, motd type and motd name causing this issue: " + motdType + "." + motd);
            }

            result.addExtra(completed);

        }

        ping.setDescriptionComponent(result);
        ping.getPlayers().setOnline(online);
        ping.getPlayers().setMax(max);
    }
}