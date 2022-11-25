package me.blueslime.pixelmotd.motd.listener.platforms;

import me.blueslime.pixelmotd.ListenerManager;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.extras.listeners.spigot.abstracts.AbstractLoginListener;
import me.blueslime.pixelmotd.extras.listeners.spigot.abstracts.AbstractTeleportListener;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.motd.listener.Ping;
import org.bukkit.event.Event;
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

    private final SlimeLogs logs;

    @SuppressWarnings("unchecked")
    public <T> SpigotListenerManager(PixelMOTD<T> plugin, SlimeLogs logs) {
        this.slimePlugin    = (PixelMOTD<JavaPlugin>) plugin;
        this.logs = logs;

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
        PluginManager manager = slimePlugin.getPlugin().getServer().getPluginManager();

        manager.registerEvents(
                loginListener,
                slimePlugin.getPlugin()
        );

        manager.registerEvents(
                teleportListener,
                slimePlugin.getPlugin()
        );

        logs.info("&9Events has been registered without issues reported! listeners loaded!");
    }

    @Override
    public void update() {

    }

    @Override
    public Ping getPing() {
        return null;
    }

}
