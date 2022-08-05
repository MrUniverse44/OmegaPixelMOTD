package me.blueslime.pixelmotd.listener.spigot;

import me.blueslime.pixelmotd.ListenerManager;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.Ping;
import me.blueslime.pixelmotd.listener.spigot.events.PacketServerPingListener;
import me.blueslime.pixelmotd.listener.spigot.events.ServerPingListener;
import me.blueslime.pixelmotd.listener.spigot.events.abstracts.AbstractLoginListener;
import me.blueslime.pixelmotd.listener.spigot.events.abstracts.AbstractTeleportListener;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class SpigotListenerManager implements ListenerManager {

    private final AbstractTeleportListener teleportListener;

    private final AbstractLoginListener loginListener;

    private final PixelMOTD<JavaPlugin> slimePlugin;

    private PacketServerPingListener packetListener;

    private ServerPingListener serverListener;

    private boolean isPacket = false;

    private final SlimeLogs logs;

    @SuppressWarnings("unchecked")
    public <T> SpigotListenerManager(PixelMOTD<T> plugin, SlimeLogs logs) {
        this.logs = logs;
        this.slimePlugin    = (PixelMOTD<JavaPlugin>) plugin;
        this.teleportListener = new AbstractTeleportListener(slimePlugin) {
            @Override
            public void execute(@NotNull Listener listener, @NotNull Event event) {
                if (event instanceof PlayerTeleportEvent) {
                    super.execute((PlayerTeleportEvent)event);
                }
            }
        };

        this.loginListener = new AbstractLoginListener(slimePlugin) {
            @Override
            public void execute(@NotNull Listener listener, @NotNull Event event) {
                if (event instanceof PlayerLoginEvent) {
                    super.execute((PlayerLoginEvent)event);
                }
            }
        };

    }

    @Override
    public void register() {
        final JavaPlugin plugin = slimePlugin.getPlugin();

        PluginManager manager = plugin.getServer().getPluginManager();

        manager.registerEvent(
                PlayerLoginEvent.class,
                loginListener,
                EventPriority.HIGH,
                loginListener,
                plugin,
                false
        );

        manager.registerEvent(
                PlayerTeleportEvent.class,
                teleportListener,
                EventPriority.HIGH,
                teleportListener,
                plugin,
                false
        );

        if (manager.isPluginEnabled("ProtocolLib")) {
            isPacket = true;

            packetListener = new PacketServerPingListener(slimePlugin);
            packetListener.register();

            logs.info("Using ProtocolLib for motds, enabling all features...");
        } else {
            isPacket = false;

            serverListener = new ServerPingListener(slimePlugin);

            manager.registerEvents(serverListener, plugin);
            logs.info("Using default motd system from minecraft, disabling some features..");
        }
    }

    @Override
    public void update() {
        loginListener.update();
        teleportListener.update();

        if (isPacket) {
            packetListener.update();
        } else {
            serverListener.update();
        }
    }

    @Override
    public Ping getPing() {
        if (isPacket) {
            return packetListener;
        }
        return serverListener;
    }

}
