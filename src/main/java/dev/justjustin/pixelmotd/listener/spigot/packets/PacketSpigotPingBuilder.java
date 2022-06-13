package dev.justjustin.pixelmotd.listener.spigot.packets;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import dev.justjustin.pixelmotd.MotdProtocol;
import dev.justjustin.pixelmotd.MotdType;
import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.listener.MotdBuilder;
import dev.justjustin.pixelmotd.listener.PingBuilder;
import dev.justjustin.pixelmotd.utils.MotdPlayers;
import dev.justjustin.pixelmotd.utils.PlaceholderParser;
import dev.mruniverse.slimelib.colors.platforms.StringSlimeColor;
import dev.mruniverse.slimelib.control.Control;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PacketSpigotPingBuilder extends PingBuilder<JavaPlugin, WrappedServerPing.CompressedImage, WrappedServerPing, WrappedGameProfile> {

    private final boolean hasPAPI;

    public PacketSpigotPingBuilder(PixelMOTD<JavaPlugin> plugin, MotdBuilder<JavaPlugin, WrappedServerPing.CompressedImage> builder) {
        super(plugin, builder);
        hasPAPI = plugin.getPlugin().getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    @Override
    public void execute(MotdType motdType, WrappedServerPing ping, int code, String user) {
        final SlimeLogs logs = getPlugin().getLogs();

        final Control control = getPlugin().getLoader().getFiles().getControl(motdType.getFile());

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
            WrappedServerPing.CompressedImage img = getBuilder().getFavicon(
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
            online = Bukkit.getServer().getOnlinePlayers().size();
        }
        if (control.getStatus(path + "players.max.toggle")) {
            String mode = control.getString(path + "players.max.type", "").toLowerCase();
            if (mode.contains("equal")) {
                max = MotdPlayers.getModeFromText(
                        control,
                        mode,
                        ping.getPlayersMaximum(),
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
            max = ping.getPlayersMaximum();
        }

        if (control.getStatus(path + "hover.toggle")) {
            ping.setPlayers(
                    Arrays.asList(getHover(
                            motdType,
                            path,
                            online,
                            max,
                            user
                    ))
            );
        }

        if (control.getStatus(path + "protocol.toggle")) {
            MotdProtocol protocol = MotdProtocol.getFromText(
                    control.getString(control.getString(path + "protocol.modifier", "")),
                    code
            );

            if (protocol == MotdProtocol.ALWAYS_POSITIVE || protocol == MotdProtocol.ALWAYS_NEGATIVE) {
                ping.setVersionProtocol(protocol.getCode());
            }

            ping.setVersionName(
                    ChatColor.translateAlternateColorCodes(
                            '&',
                            control.getString(path + "protocol.message")
                    )
            );
        }

        if (!motdType.isHexMotd()) {
            line1 = control.getColoredString(path + "line1", "");
            line2 = control.getColoredString(path + "line2", "");

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

        } else {
            line1 = control.getString(path + "line1", "");
            line2 = control.getString(path + "line2", "");

            if (hasPAPI) {
                line1 = PlaceholderParser.parse(getPlugin().getLogs(), user, line1);
                line2 = PlaceholderParser.parse(getPlugin().getLogs(), user, line2);
            }

            completed = new StringSlimeColor(
                    getExtras().replace(
                            line1,
                            online,
                            max,
                            user
                    ) + "\n" + getExtras().replace(
                            line2,
                            online,
                            max,
                            user
                    ),
                    true
            ).build();

            completed = ChatColor.translateAlternateColorCodes('&', completed);
        }

        ping.setMotD(completed);
        ping.setPlayersOnline(online);
        ping.setPlayersMaximum(max);
    }

    @Override
    public WrappedGameProfile[] getHover(MotdType motdType, String path, int online, int max, String user) {
        Control control = getPlugin().getLoader().getFiles().getControl(motdType.getFile());

        WrappedGameProfile[] gameProfiles = new WrappedGameProfile[0];

        List<String> lines;

        if (isPlayerSystem()) {
            lines = getExtras().replaceHoverLine(
                    control.getColoredStringList(path + "hover.lines"),
                    control.getInt(path + "hover.hasMoreOnline")
            );
        } else {
            lines = control.getColoredStringList(path + "hover.lines");
        }

        final UUID uuid = UUID.fromString("0-0-0-0-0");

        for (String line : lines) {
            gameProfiles = addHoverLine(
                    gameProfiles,
                    new WrappedGameProfile(
                            uuid,
                            getExtras().replace(line, online, max, user)
                    )
            );
        }
        return gameProfiles;
    }

    @Override
    public WrappedGameProfile[] addHoverLine(WrappedGameProfile[] player, WrappedGameProfile info) {
        WrappedGameProfile[] gameProfiles = new WrappedGameProfile[player.length + 1];
        System.arraycopy(player, 0, gameProfiles, 0, player.length);
        gameProfiles[player.length] = info;
        return gameProfiles;
    }
}
