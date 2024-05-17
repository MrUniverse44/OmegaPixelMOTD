package me.blueslime.omegapixelmotd.modules.listeners.bungeecord.list;

import com.comphenix.protocol.PacketType;
import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.omegapixelmotd.modules.configurations.Configurations;
import me.blueslime.omegapixelmotd.modules.listeners.ping.BungeecordPingListener;
import me.blueslime.omegapixelmotd.modules.motds.Motd;
import me.blueslime.omegapixelmotd.modules.motds.MotdData;
import me.blueslime.omegapixelmotd.utils.text.TextReplacer;
import me.blueslime.wardenplugin.colors.ColorHandler;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ProxyPingListener extends BungeecordPingListener implements Listener {
    private boolean isWhitelistEnabled;
    private String unknownPlayer;
    private int defaultProtocol;

    public ProxyPingListener(OmegaPixelMOTD plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        super.initialize();

        Plugin plugin = getOriginPlugin();
        plugin.getProxy().getPluginManager().registerListener(plugin, this);
        reload();
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }

    @Override
    public void reload() {
        super.reload();
        Configurations configurations = this.plugin.getModule(Configurations.class);

        this.unknownPlayer = configurations.getSettings().getString("settings.unknown-player", "Unknown Player");
        this.isWhitelistEnabled = configurations.getWhitelist().getBoolean("current-state.enabled", false);
        this.defaultProtocol = configurations.getSettings().getInt("bukkit-exclusive.default-protocol", 762);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(ProxyPingEvent event) {
        int protocol = event.getConnection().getVersion();

        if (isWhitelistEnabled) {
            execute(MotdData.Type.WHITELIST, event, protocol);
            return;
        }

        execute(MotdData.Type.NORMAL, event, protocol);
    }

    public void execute(MotdData.Type type, ProxyPingEvent eventHandler, int protocol) {
        Motd motd = fetchMotd(type, protocol);

        final ServerPing event = eventHandler.getResponse();

        if (event == null || event instanceof Cancellable && ((Cancellable) event).isCancelled()) {
            getLogs().debug("The plugin is receiving a null ping from proxy, this issue is not caused by PixelMOTD");
            return;
        }

        if (motd == null) {
            getLogs().info("No motds will be displayed because no motds found for type: " + type.toString() + " and protocol: " + protocol);
            return;
        }

        String line1, line2, completed;

        int max;
        int online;

        if (motd.hasServerIcon()) {
            Favicon favicon = getFavicon(
                motd.getServerIcon()
            );

            if (favicon != null) {
                event.setFavicon(
                    favicon
                );
            }
        }

        max = motd.getMaxPlayers(event.getPlayers().getMax(), event.getPlayers().getOnline());

        TextReplacer replacer = TextReplacer.builder()
            .replace("<online>", event.getPlayers().getOnline())
            .replace("<max>", String.valueOf(max))
            .replace("<player>", unknownPlayer)
            .replace("<protocol>", String.valueOf(defaultProtocol));

        online = motd.getOnlinePlayers(event.getPlayers().getOnline(), event.getPlayers().getMax());

        line1 = ColorHandler.convert(motd.getLine1(replacer));
        line2 = ColorHandler.convert(motd.getLine2(replacer));

        completed = line1 + "\n" + line2;

        event.setDescriptionComponent(
            new TextComponent(
                completed
            )
        );

        ServerPing.Players players = event.getPlayers();

        players.setMax(max);
        players.setOnline(online);

        if (motd.hasHover()) {
            players.setSample(
                    hover.convert(
                            hover.generate(motd, replacer)
                    )
            );
        }

        if (motd.hasProtocols()) {
            if (motd.getProtocol() == MotdData.Protocol.ALWAYS_POSITIVE) {
                event.getVersion().setProtocol(protocol);
            } else if (motd.getProtocol() == MotdData.Protocol.ALWAYS_NEGATIVE) {
                event.getVersion().setProtocol(-1);
            }

            event.getVersion().setName(
                    ColorHandler.convert(
                            motd.getProtocolMessage(replacer)
                    )
            );
        }

        eventHandler.setResponse(event);
    }
}
