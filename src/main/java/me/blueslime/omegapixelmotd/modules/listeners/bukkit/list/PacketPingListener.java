package me.blueslime.omegapixelmotd.modules.listeners.bukkit.list;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.omegapixelmotd.modules.configurations.Configurations;
import me.blueslime.omegapixelmotd.modules.listeners.ping.ProtocolPingListener;
import me.blueslime.omegapixelmotd.modules.motds.Motd;
import me.blueslime.omegapixelmotd.modules.motds.MotdData;
import me.blueslime.omegapixelmotd.modules.placeholders.parser.PlaceholderParser;
import me.blueslime.omegapixelmotd.utils.text.TextReplacer;
import me.blueslime.wardenplugin.colors.ColorHandler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketPingListener extends ProtocolPingListener {
    private boolean hasPlaceholders;

    private boolean isWhitelistEnabled;
    private String unknownPlayer;
    private int defaultProtocol;

    public PacketPingListener(OmegaPixelMOTD plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        super.initialize();

        ProtocolLibrary.getProtocolManager().addPacketListener(this);
        reload();
    }

    @Override
    public void shutdown() {
        super.shutdown();

        ProtocolLibrary.getProtocolManager().removePacketListener(this);
    }

    @Override
    public void reload() {
        super.reload();

        Configurations configurations = this.plugin.getModule(Configurations.class);

        JavaPlugin plugin = getOriginPlugin();
        this.hasPlaceholders = plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
        this.unknownPlayer = configurations.getSettings().getString("settings.unknown-player", "Unknown Player");
        this.isWhitelistEnabled = configurations.getWhitelist().getBoolean("current-state.enabled", false);
        this.defaultProtocol = configurations.getSettings().getInt("bukkit-exclusive.default-protocol", 762);
    }

    @Override
    public void onPacketSending(final PacketEvent event) {
        if (event.getPacketType() != PacketType.Status.Server.SERVER_INFO) {
            getLogs().debug("The plugin is receiving a different packet of SERVER_INFO, the received packet is: " + event.getPacketType());
            return;
        }

        if (event.isCancelled()) {
            getLogs().debug("Another plugin is cancelling your motd event, the plugin can't show the motd :(");
            return;
        }

        int protocol;

        if (event.getPlayer() != null) {
            protocol = getProtocol(event.getPlayer());
        } else {
            protocol = defaultProtocol;
        }

        if (isWhitelistEnabled) {
            execute(MotdData.Type.WHITELIST, event, protocol);
            return;
        }

        execute(MotdData.Type.NORMAL, event, protocol);
    }

    public void execute(MotdData.Type type, PacketEvent eventHandler, int protocol) {
        int index = 0;
        WrappedServerPing event = eventHandler.getPacket().getServerPings().read(index);

        Motd motd = fetchMotd(type, defaultProtocol);

        if (event == null) {
            getLogs().debug("The ping was null in the index 0, searching in another index");
            if (eventHandler.getPacket().getServerPings().size() > 2) {
                index = 1;
                event = eventHandler.getPacket().getServerPings().read(index);
                getLogs().debug("Currently reading index 1 of ping packet.");
            }
        }

        if (event == null) {
            getLogs().debug("The plugin is receiving a null ping from ProtocolLib, please report it to ProtocolLib, this issue is not caused by PixelMOTD");
            return;
        }

        if (motd == null) {
            getLogs().info("No motds will be displayed because no motds found for type: " + type.toString() + " and protocol: " + defaultProtocol);
            return;
        }

        String line1, line2, completed;

        int max;
        int online;

        if (motd.hasServerIcon()) {
            WrappedServerPing.CompressedImage favicon = getFavicon(
                motd.getServerIcon()
            );

            if (favicon != null) {
                event.setFavicon(
                    favicon
                );
            }
        }

        max = motd.getMaxPlayers(event.getPlayersMaximum(), event.getPlayersOnline());

        TextReplacer replacer = TextReplacer.builder()
            .replace("<online>", event.getPlayersOnline())
            .replace("<max>", String.valueOf(max))
            .replace("<player>", unknownPlayer)
            .replace("<protocol>", String.valueOf(defaultProtocol));

        online = motd.getOnlinePlayers(event.getPlayersOnline(), event.getPlayersMaximum());

        line1 = ColorHandler.convert(motd.getLine1(replacer));
        line2 = ColorHandler.convert(motd.getLine2(replacer));

        if (hasPlaceholders) {
            line1 = PlaceholderParser.parse(line1);
            line2 = PlaceholderParser.parse(line2);
        }

        completed = line1 + "\n" + line2;

        event.setMotD(
            hasPlaceholders ? PlaceholderParser.parse(completed) : completed
        );

        event.setPlayersMaximum(max);
        event.setPlayersOnline(online);

        event.setPlayers(hover.generate(motd, replacer, true));

        if (motd.getProtocol() == MotdData.Protocol.ALWAYS_POSITIVE) {
            event.setVersionProtocol(protocol);
        } else if (motd.getProtocol() == MotdData.Protocol.ALWAYS_NEGATIVE) {
            event.setVersionProtocol(-1);
        }

        event.setVersionName(
            ColorHandler.convert(
                hasPlaceholders ?
                    PlaceholderParser.parse(motd.getProtocolMessage(replacer)) :
                    motd.getProtocolMessage(replacer)
            )
        );
        eventHandler.getPacket().getServerPings().write(index, event);
    }

    public int getProtocol(Player player) {
        return ProtocolLibrary.getProtocolManager().getProtocolVersion(player);
    }
}
