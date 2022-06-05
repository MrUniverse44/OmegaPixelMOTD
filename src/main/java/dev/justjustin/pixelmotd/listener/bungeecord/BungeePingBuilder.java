package dev.justjustin.pixelmotd.listener.bungeecord;

import dev.justjustin.pixelmotd.MotdProtocol;
import dev.justjustin.pixelmotd.MotdType;
import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.listener.MotdBuilder;
import dev.justjustin.pixelmotd.listener.PingBuilder;
import dev.justjustin.pixelmotd.utils.MotdPlayers;
import dev.mruniverse.slimelib.colors.platforms.bungeecord.BungeeSlimeColor;
import dev.mruniverse.slimelib.control.Control;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import dev.mruniverse.slimelib.utils.ClassUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.List;
import java.util.UUID;

public class BungeePingBuilder extends PingBuilder<Plugin, Favicon, ServerPing, ServerPing.PlayerInfo> {

    private static final boolean HAS_RGB_SUPPORT = ClassUtils.hasMethod(ChatColor.class, "of", String.class);

    public BungeePingBuilder(PixelMOTD<Plugin> plugin, MotdBuilder<Plugin, Favicon> builder) {
        super(plugin, builder);
    }

    @Override
    public void execute(MotdType motdType, ServerPing ping, int code, String user) {
        final SlimeLogs logs = getPlugin().getLogs();

        final Control control = getPlugin().getLoader().getFiles().getControl(motdType.getFile());

        String motd;

        try {
            motd = getMotd(motdType);
        } catch (Exception ignored) {
            logs.error("This file isn't updated to the latest file or the motd-path is incorrect, can't find motds for MotdType: " + motdType.toString());
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
                ping.setFavicon(img);
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
            online = ProxyServer.getInstance().getOnlineCount();
        }
        if (control.getStatus(path + "players.max.toggle")) {
            String mode = control.getString(path + "players.max.type", "").toLowerCase();
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

        if (control.getStatus(path + "hover.toggle")) {
            ping.getPlayers().setSample(
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

            if (protocol == MotdProtocol.ALWAYS_POSITIVE || protocol == MotdProtocol.ALWAYS_NEGATIVE) {
                ping.getVersion().setProtocol(protocol.getCode());
            }

            ping.getVersion().setName(
                    ChatColor.translateAlternateColorCodes(
                            '&',
                            control.getString(path + "protocol.message")
                    )
            );
        }

        TextComponent result = new TextComponent("");

        if (motdType.isHexMotd()) {

            line1 = control.getString(path + "line1", "");
            line2 = control.getString(path + "line2", "");

            completed = line1 + "\n" + line2;

            result.addExtra(
                    new BungeeSlimeColor(completed, HAS_RGB_SUPPORT)
                            .build()
            );

        } else {

            line1 = control.getColoredString(path + "line1", "");
            line2 = control.getColoredString(path + "line2", "");

            completed = line1 + "\n" + line2;

            result.addExtra(completed);

        }

        ping.setDescriptionComponent(result);
        ping.getPlayers().setOnline(online);
        ping.getPlayers().setMax(max);
    }

    @Override
    public ServerPing.PlayerInfo[] getHover(MotdType motdType, String path, int online, int max, String user) {
        ServerPing.PlayerInfo[] hoverToShow = new ServerPing.PlayerInfo[0];
        List<String> lines;

        Control control = getPlugin().getLoader().getFiles().getControl(motdType.getFile());

        lines = control.getColoredStringList(path + "hover.lines");

        final UUID uuid = UUID.fromString("0-0-0-0-0");

        for (String line : lines) {
            hoverToShow = addHoverLine(hoverToShow, new ServerPing.PlayerInfo(line, uuid));
        }

        return hoverToShow;
    }

    @Override
    public ServerPing.PlayerInfo[] addHoverLine(ServerPing.PlayerInfo[] player, ServerPing.PlayerInfo info) {
        ServerPing.PlayerInfo[] hoverText = new ServerPing.PlayerInfo[player.length + 1];
        System.arraycopy(player, 0, hoverText, 0, player.length);
        hoverText[player.length] = info;
        return hoverText;
    }
}
