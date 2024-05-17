package me.blueslime.omegapixelmotd.modules.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.wardenplugin.WardenPlugin;
import me.blueslime.wardenplugin.logs.WardenLogs;
import org.bukkit.plugin.Plugin;

public abstract class ProtocolListener extends PacketAdapter implements Listener {
    protected final OmegaPixelMOTD plugin;

    public ProtocolListener(final OmegaPixelMOTD plugin, ListenerPriority priority, PacketType... types) {
        super((Plugin) plugin.getPlugin(), priority, types);
        this.plugin = plugin;
    }

    public abstract void initialize();
    public abstract void shutdown();
    public abstract void reload();

    public <T> T getOriginPlugin() {
        if (plugin == null || plugin.getPlugin() == null) {
            return null;
        }

        WardenPlugin<T> plugin = this.plugin.cast();

        return plugin.getPlugin();
    }

    public WardenLogs getLogs() {
        return plugin.getLogs();
    }
}