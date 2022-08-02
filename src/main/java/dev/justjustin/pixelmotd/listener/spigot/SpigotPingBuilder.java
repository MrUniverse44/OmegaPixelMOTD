package dev.justjustin.pixelmotd.listener.spigot;

import dev.justjustin.pixelmotd.MotdType;
import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.iridiumcolorapi.IridiumColorAPI;
import dev.justjustin.pixelmotd.listener.MotdBuilder;
import dev.justjustin.pixelmotd.listener.PingBuilder;
import dev.justjustin.pixelmotd.utils.MotdPlayers;
import dev.justjustin.pixelmotd.utils.PlaceholderParser;
import dev.mruniverse.slimelib.colors.platforms.StringSlimeColor;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.file.configuration.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.CachedServerIcon;

public class SpigotPingBuilder extends PingBuilder<JavaPlugin, CachedServerIcon, ServerListPingEvent, EmptyPlayerInfo> {

    private final boolean hasPAPI;
    public SpigotPingBuilder(PixelMOTD<JavaPlugin> plugin, MotdBuilder<JavaPlugin, CachedServerIcon> builder) {
        super(plugin, builder);
        hasPAPI = plugin.getPlugin().getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    @Override
    public void execute(MotdType motdType, ServerListPingEvent ping, int code, String user) {
        final ConfigurationHandler control = getPlugin().getConfigurationHandler(motdType.getFile());

        String motd = getMotd(motdType);

        String line1, line2, completed;

        int max;

        String path = motdType + "." + motd + ".";

        if (isIconSystem()) {
            CachedServerIcon img = getBuilder().getFavicon(
                    motdType,
                    control.getString(
                        path + "icons.icon"
                    )
            );

            if (img != null) {
                ping.setServerIcon(img);
            }
        }

        if (control.getStatus(path + "players.max.toggle")) {
            String mode = control.getString(path + "players.max.type", "").toLowerCase();
            if (mode.contains("equal")) {
                max = MotdPlayers.getModeFromText(
                        control,
                        mode,
                        ping.getMaxPlayers(),
                        path + "players.max."
                );
            } else {
                max = MotdPlayers.getModeFromText(
                        control,
                        mode,
                        ping.getNumPlayers(),
                        path + "players.max."
                );
            }
        } else {
            max = ping.getMaxPlayers();
        }

        if (!motdType.isHexMotd()) {
            line1 = control.getString(TextDecoration.LEGACY, path + "line1", "");
            line2 = control.getString(TextDecoration.LEGACY, path + "line2", "");

            if (hasPAPI) {
                line1 = PlaceholderParser.parse(getPlugin().getLogs(), user, line1);
                line2 = PlaceholderParser.parse(getPlugin().getLogs(), user, line2);
            }

            completed = getExtras().replace(
                    line1,
                    ping.getNumPlayers(),
                    ping.getMaxPlayers(),
                    user
            ) + "\n" + getExtras().replace(
                    line2,
                    ping.getNumPlayers(),
                    ping.getMaxPlayers(),
                    user
            );

        } else {
            line1 = control.getString(path + "line1", "");
            line2 = control.getString(path + "line2", "");

            if (hasPAPI) {
                line1 = PlaceholderParser.parse(getPlugin().getLogs(), user, line1);
                line2 = PlaceholderParser.parse(getPlugin().getLogs(), user, line2);
            }

            completed = getExtras().replace(
                    line1,
                    ping.getNumPlayers(),
                    ping.getMaxPlayers(),
                    user
            ) + "\n" + getExtras().replace(
                    line2,
                    ping.getNumPlayers(),
                    ping.getMaxPlayers(),
                    user
            );

            if (!completed.contains("<GRADIENT:") && !completed.contains("<RAINBOW") && !completed.contains("<SOLID:")) {

                completed = new StringSlimeColor(
                        completed,
                        true
                ).build();

            } else {

                completed = IridiumColorAPI.process(completed);

            }


            completed = ChatColor.translateAlternateColorCodes('&', completed);
        }

        ping.setMotd(completed);
        ping.setMaxPlayers(max);

    }

    @Override
    public EmptyPlayerInfo[] getHover(MotdType motdType, String path, int online, int max, String user) {
        return new EmptyPlayerInfo[0];
    }

    @Override
    public EmptyPlayerInfo[] addHoverLine(EmptyPlayerInfo[] player, EmptyPlayerInfo info) {
        return new EmptyPlayerInfo[0];
    }
}
