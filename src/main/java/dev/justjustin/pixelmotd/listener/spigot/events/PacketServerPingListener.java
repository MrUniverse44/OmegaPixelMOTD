package dev.justjustin.pixelmotd.listener.spigot.events;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import dev.mruniverse.slimelib.SlimePlugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketServerPingListener extends PacketAdapter {

    private final SlimePlugin<JavaPlugin> slimePlugin;

    public PacketServerPingListener(SlimePlugin<JavaPlugin> slimePlugin) {
        super(slimePlugin.getPlugin(), ListenerPriority.HIGHEST, PacketType.Status.Server.SERVER_INFO);

        this.slimePlugin = slimePlugin;
    }

    public void register() {
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }

    @Override
    public void onPacketSending(final PacketEvent event) {
        if (event.getPacketType() != PacketType.Status.Server.SERVER_INFO) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        if (event.getPlayer() == null) {
            return;
        }

        final WrappedServerPing ping = event.getPacket().getServerPings().read(0);

        if (ping == null) {
            return;
        }

        // TODO: Motd
    }

}
