package me.blueslime.omegapixelmotd.modules.listeners.bukkit;

import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.omegapixelmotd.modules.listeners.Listeners;
import me.blueslime.omegapixelmotd.modules.listeners.bukkit.list.PacketPingListener;
import me.blueslime.omegapixelmotd.modules.listeners.bukkit.list.ServerPingListener;
import me.blueslime.wardenplugin.modules.list.BukkitModule;

public class BukkitListeners extends BukkitModule {
    private final Listeners listeners = new Listeners();
    private final OmegaPixelMOTD plugin;

    public BukkitListeners(OmegaPixelMOTD plugin) {
        super(plugin.cast());
        this.plugin = plugin;
    }

    @Override
    public void initialize() {
        boolean packets = getPlugin().getPlugin().getServer().getPluginManager().isPluginEnabled("ProtocolLib");

        listeners.registerListener(
            packets ? new PacketPingListener(plugin) : new ServerPingListener(plugin)
        );

        listeners.initialize();
    }

    @Override
    public void shutdown() {
        listeners.shutdown();
    }

    @Override
    public void reload() {
        listeners.reload();
    }
}
