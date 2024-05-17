package me.blueslime.omegapixelmotd.modules.listeners.bukkit.list;

import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.omegapixelmotd.modules.configurations.Configurations;
import me.blueslime.omegapixelmotd.modules.listeners.ping.BukkitPingListener;
import me.blueslime.omegapixelmotd.modules.motds.Motd;
import me.blueslime.omegapixelmotd.modules.motds.MotdData;
import me.blueslime.omegapixelmotd.modules.placeholders.parser.PlaceholderParser;
import me.blueslime.omegapixelmotd.utils.text.TextReplacer;
import me.blueslime.wardenplugin.colors.ColorHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.CachedServerIcon;

public class ServerPingListener extends BukkitPingListener implements Listener {

    private boolean hasPlaceholders;

    private boolean isWhitelistEnabled;
    private String unknownPlayer;

    private int defaultProtocol;

    public ServerPingListener(OmegaPixelMOTD plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        super.initialize();

        JavaPlugin plugin = getOriginPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        Configurations configurations = this.plugin.getModule(Configurations.class);

        this.hasPlaceholders = plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
        this.unknownPlayer = configurations.getSettings().getString("settings.unknown-player", "Unknown Player");
        this.isWhitelistEnabled = configurations.getWhitelist().getBoolean("current-state.enabled", false);
        this.defaultProtocol = configurations.getSettings().getInt("bukkit-exclusive.default-protocol", 762);
    }

    @Override
    public void reload() {
        super.reload();
        Configurations configurations = this.plugin.getModule(Configurations.class);

        this.unknownPlayer = configurations.getSettings().getString("settings.unknown-player", "Unknown Player");
        this.isWhitelistEnabled = configurations.getWhitelist().getBoolean("current-state.enabled", false);
        this.defaultProtocol = configurations.getSettings().getInt("bukkit-exclusive.default-protocol", 762);
    }

    @EventHandler
    public void on(ServerListPingEvent event) {
        if (isWhitelistEnabled) {
            execute(MotdData.Type.WHITELIST, event);
            return;
        }

        execute(MotdData.Type.NORMAL, event);
    }

    public void execute(MotdData.Type type, ServerListPingEvent event) {
        Motd motd = fetchMotd(type, defaultProtocol);

        if (motd == null) {
            getLogs().info("No motds will be displayed because no motds found for type: " + type.toString() + " and protocol: " + defaultProtocol);
            return;
        }

        String line1, line2, completed;

        int max;

        if (motd.hasServerIcon()) {
            CachedServerIcon favicon = getFavicon(
                motd.getServerIcon()
            );

            if (favicon != null) {
                event.setServerIcon(
                    favicon
                );
            }
        }

        max = motd.getMaxPlayers(event.getMaxPlayers(), event.getNumPlayers());

        TextReplacer replacer = TextReplacer.builder()
            .replace("<online>", event.getNumPlayers())
            .replace("<max>", String.valueOf(max))
            .replace("<player>", unknownPlayer)
            .replace("<protocol>", String.valueOf(defaultProtocol));

        line1 = ColorHandler.convert(motd.getLine1(replacer));
        line2 = ColorHandler.convert(motd.getLine2(replacer));

        if (hasPlaceholders) {
            line1 = PlaceholderParser.parse(line1);
            line2 = PlaceholderParser.parse(line2);
        }

        completed = line1 + "\n" + line2;

        event.setMotd(
            completed
        );

        event.setMaxPlayers(max);
    }
}
