package dev.justjustin.pixelmotd.listener.spigot;

import dev.justjustin.pixelmotd.MotdType;
import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.SlimeFile;
import dev.justjustin.pixelmotd.listener.MotdBuilder;
import dev.justjustin.pixelmotd.listener.PingBuilder;
import dev.justjustin.pixelmotd.utils.MotdPlayers;
import dev.justjustin.pixelmotd.utils.PlaceholderParser;
import dev.mruniverse.slimelib.control.Control;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.CachedServerIcon;

public class SpigotPingBuilder extends PingBuilder<JavaPlugin, CachedServerIcon, ServerListPingEvent, SlimeFile> {

    private final boolean hasPAPI;
    public SpigotPingBuilder(PixelMOTD<JavaPlugin> plugin, MotdBuilder<JavaPlugin, CachedServerIcon> builder) {
        super(plugin, builder);
        hasPAPI = plugin.getPlugin().getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    @Override
    public void execute(MotdType motdType, ServerListPingEvent ping, int code, String user) {
        final Control control = getPlugin().getLoader().getFiles().getControl(motdType.getFile());

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

        line1 = control.getColoredString(path + "line1", "");
        line2 = control.getColoredString(path + "line2", "");

        if (hasPAPI) {
            line1 = PlaceholderParser.parse(getPlugin().getLogs(), user, line1);
            line2 = PlaceholderParser.parse(getPlugin().getLogs(), user, line2);
        }

        completed = line1 + "\n" + line2;

        ping.setMotd(completed);
        ping.setMaxPlayers(max);

    }
}
