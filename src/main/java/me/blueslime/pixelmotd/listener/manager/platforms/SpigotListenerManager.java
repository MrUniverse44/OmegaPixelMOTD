package me.blueslime.pixelmotd.listener.manager.platforms;

import me.blueslime.pixelmotd.listener.manager.ListenerManager;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.extras.listeners.spigot.abstracts.AbstractLoginListener;
import me.blueslime.pixelmotd.extras.listeners.spigot.abstracts.AbstractTeleportListener;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class SpigotListenerManager extends ListenerManager<JavaPlugin> {

    private final AbstractTeleportListener teleportListener;

    private final AbstractLoginListener loginListener;

    public SpigotListenerManager(PixelMOTD<?> plugin, SlimeLogs logs) {
        super(plugin, logs);

        this.teleportListener = new AbstractTeleportListener(getPlugin()) {
            @Override
            public void execute(@NotNull Listener listener, @NotNull Event event) {
                if (event instanceof PlayerTeleportEvent) {
                    super.execute((PlayerTeleportEvent)event);
                }
            }
        };

        this.loginListener = new AbstractLoginListener(getPlugin()) {
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
        PluginManager manager = getPlugin().getPlugin().getServer().getPluginManager();

        manager.registerEvents(
                loginListener,
                getPlugin().getPlugin()
        );

        manager.registerEvents(
                teleportListener,
                getPlugin().getPlugin()
        );

        getLogs().info("&9Events has been registered without issues reported! listeners loaded!");
    }

    @Override
    public void update() {

    }

}
