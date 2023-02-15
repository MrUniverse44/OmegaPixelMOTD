package me.blueslime.pixelmotd.motd.manager.platforms;

import me.blueslime.pixelmotd.motd.manager.ListenerManager;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.spigot.events.PacketServerPingListener;
import me.blueslime.pixelmotd.listener.spigot.events.ServerPingListener;
import me.blueslime.pixelmotd.listener.spigot.events.abstracts.AbstractLoginListener;
import me.blueslime.pixelmotd.listener.spigot.events.abstracts.AbstractTeleportListener;
import me.blueslime.pixelmotd.utils.Extras;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class BukkitListenerManager extends ListenerManager<JavaPlugin> {

    private final AbstractTeleportListener teleport;

    private final AbstractLoginListener login;

    private PacketServerPingListener protocol;

    private ServerPingListener listener;

    private boolean isPacket = false;

    public BukkitListenerManager(PixelMOTD<JavaPlugin> plugin) {
        super(plugin);

        this.teleport = new AbstractTeleportListener(plugin) {
            @Override
            public void execute(@NotNull Listener listener, @NotNull Event event) {
                if (event instanceof PlayerTeleportEvent) {
                    super.execute((PlayerTeleportEvent)event);
                }
            }
        };

        this.login = new AbstractLoginListener(plugin) {
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
        final JavaPlugin plugin = getPlugin().getPlugin();

        PluginManager manager = plugin.getServer().getPluginManager();

        manager.registerEvent(
                PlayerLoginEvent.class,
                login,
                EventPriority.HIGH,
                login,
                plugin,
                false
        );

        manager.registerEvent(
                PlayerTeleportEvent.class,
                teleport,
                EventPriority.HIGH,
                teleport,
                plugin,
                false
        );

        if (manager.isPluginEnabled("ProtocolLib")) {
            isPacket = true;

            protocol = new PacketServerPingListener(getPlugin());
            protocol.register();

            getLogs().info("Using ProtocolLib for motds, enabling all features...");
        } else {
            isPacket = false;

            listener = new ServerPingListener(getPlugin());

            manager.registerEvents(listener, plugin);
            getLogs().info("Using default motd system from minecraft, disabling some features..");
        }
    }

    @Override
    public void update() {
        teleport.update();
        login.update();

        if (isPacket) {
            protocol.update();
        } else {
            listener.update();
        }
    }

    @Override
    public boolean isPlayer() {
        if (isPacket) {
            return protocol.getPingBuilder().isPlayerSystem();
        }
        return listener.getPingBuilder().isPlayerSystem();
    }

    @Override
    public Extras getExtras() {
        if (isPacket) {
            return protocol.getPingBuilder().getExtras();
        }
        return listener.getPingBuilder().getExtras();
    }

}
