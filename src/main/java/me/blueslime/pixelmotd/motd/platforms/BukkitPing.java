package me.blueslime.pixelmotd.motd.platforms;

import me.blueslime.pixelmotd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.external.iridiumcolorapi.IridiumColorAPI;
import me.blueslime.pixelmotd.motd.builder.hover.EmptyPlayerInfo;
import me.blueslime.pixelmotd.motd.builder.favicon.FaviconModule;
import me.blueslime.pixelmotd.motd.builder.PingBuilder;
import me.blueslime.pixelmotd.motd.builder.hover.HoverModule;
import me.blueslime.pixelmotd.utils.MotdPlayers;
import me.blueslime.pixelmotd.utils.PlaceholderParser;
import dev.mruniverse.slimelib.colors.platforms.StringSlimeColor;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.file.configuration.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.CachedServerIcon;

public class BukkitPing extends PingBuilder<JavaPlugin, CachedServerIcon, ServerListPingEvent, EmptyPlayerInfo> {

    private final boolean hasPAPI;

    public BukkitPing(
            PixelMOTD<JavaPlugin> plugin,
            FaviconModule<JavaPlugin, CachedServerIcon> builder,
            HoverModule<EmptyPlayerInfo> hoverModule
    ) {
        super(plugin, builder, hoverModule);

        hasPAPI = plugin.getPlugin().getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    @Override
    public void execute(MotdType motdType, ServerListPingEvent ping, int code, String user) {
        final ConfigurationHandler control = getPlugin().getConfigurationHandler(motdType.getFile());

        String motd = getMotd(motdType);

        if (motd.equals("8293829382382732127413475y42732749832748327472fyfs")) {
            if (isDebug()) {
                getPlugin().getLogs().debug("The plugin don't detect motds for MotdType: " + motdType);
            }
            return;
        }

        String line1, line2, completed;

        int max;

        String path = motdType + "." + motd + ".";

        if (isIconSystem()) {
            String iconName = control.getString(
                    path + "icons.icon", ""
            );
            if (!iconName.equalsIgnoreCase("") && !iconName.equalsIgnoreCase("disabled")) {
                CachedServerIcon img = getFavicon().getFavicon(
                        iconName
                );

                if (img != null) {
                    ping.setServerIcon(img);
                }
            }
        }

        if (control.getStatus(path + "players.max.toggle", false)) {
            String mode = control.getString(path + "players.max.type", "add").toLowerCase();
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
}

