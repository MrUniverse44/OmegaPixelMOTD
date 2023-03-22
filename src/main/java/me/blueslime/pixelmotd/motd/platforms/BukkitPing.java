package me.blueslime.pixelmotd.motd.platforms;

import me.blueslime.pixelmotd.motd.CachedMotd;
import me.blueslime.pixelmotd.motd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.external.iridiumcolorapi.IridiumColorAPI;
import me.blueslime.pixelmotd.motd.builder.hover.EmptyPlayerInfo;
import me.blueslime.pixelmotd.motd.builder.favicon.FaviconModule;
import me.blueslime.pixelmotd.motd.builder.PingBuilder;
import me.blueslime.pixelmotd.motd.builder.hover.HoverModule;
import me.blueslime.pixelmotd.utils.PlaceholderParser;
import dev.mruniverse.slimelib.colors.platforms.StringSlimeColor;
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
        CachedMotd motd = getMotd(motdType);

        if (motd == null) {
            if (isDebug()) {
                getLogs().debug("The plugin don't detect motds for MotdType: " + motdType);
            }
            return;
        }

        String line1, line2, completed;

        int max;

        if (isIconSystem()) {
            if (motd.hasServerIcon()) {
                CachedServerIcon favicon = getFavicon().getFavicon(
                        motd.getServerIcon()
                );

                if (favicon != null) {
                    ping.setServerIcon(
                            favicon
                    );
                }
            }
        }

        max = motd.getMax(getPlugin(), ping.getNumPlayers());

        if (!motd.hasHex()) {
            line1 = ChatColor.translateAlternateColorCodes('&', motd.getLine1());
            line2 = ChatColor.translateAlternateColorCodes('&', motd.getLine2());

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
            line1 = motd.getLine1();
            line2 = motd.getLine2();

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

        ping.setMotd(
                ChatColor.translateAlternateColorCodes('&', completed)
        );
        ping.setMaxPlayers(max);

    }
}

