package me.blueslime.pixelmotd.listener.manager;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.manager.platforms.*;
import dev.mruniverse.slimelib.SlimePlatform;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.players.PlayerDatabase;

public abstract class ListenerManager<P> {

    public static final boolean IS_BUKKIT = isBukkit();

    public static final boolean IS_PAPER = isPaper();

    private final PixelMOTD<P> plugin;

    private final SlimeLogs logs;

    @SuppressWarnings("unchecked")
    public ListenerManager(PixelMOTD<?> plugin, SlimeLogs logs) {
        this.plugin = (PixelMOTD<P>) plugin;
        this.logs = logs;
    }

    public static boolean isPaper() {
        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static boolean isBukkit() {
        try {
            Class.forName("org.bukkit.entity.Player$Spigot");
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    private final PlayerDatabase database = new PlayerDatabase();

    /**
     * Calls a new ListenerManager for the plugin depending on the platform
     * @param platform Server Platform
     * @param plugin Main class instance
     * @param logs Logs to use for information
     * @return a new Listener Manager
     * @param <T> the platform-main-class, example: JavaPlugin (Bukkit), Plugin (Bungee), ProxyServer (Velocity), Server (Sponge)
     */
    @SuppressWarnings("unchecked")
    public static <T> ListenerManager<T> createNewInstance(SlimePlatform platform, PixelMOTD<T> plugin, SlimeLogs logs) {
        switch (platform) {
            case SPIGOT:
                if (IS_PAPER) {
                    return (ListenerManager<T>) new PaperListenerManager(plugin, logs);
                }
                if (IS_BUKKIT) {
                    return (ListenerManager<T>)  new BukkitListenerManager(plugin, logs);
                } else {
                    return (ListenerManager<T>) new SpigotListenerManager(plugin, logs);
                }
            default:
            case BUNGEECORD:
                return (ListenerManager<T>) new BungeeListenerManager(plugin, logs);
            case SPONGE:
                return (ListenerManager<T>) new SpongeListenerManager(plugin, logs);
            case VELOCITY:
                return (ListenerManager<T>) new VelocityListenerManager(plugin, logs);
        }
    }

    public PixelMOTD<P> getPlugin() {
        return plugin;
    }

    /**
     * Register the default listener
     */
    public abstract void register();

    public abstract void update();

    public PlayerDatabase getDatabase() {
        return database;
    }

    public SlimeLogs getLogs() {
        return logs;
    }
}
