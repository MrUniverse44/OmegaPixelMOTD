package me.blueslime.pixelmotd.metrics;

import me.blueslime.pixelmotd.metrics.bungeecord.BungeeCordMetricsHandler;
import me.blueslime.pixelmotd.metrics.bukkit.BukkitMetricsHandler;
import me.blueslime.pixelmotd.metrics.sponge.SpongeMetricsHandler;
import me.blueslime.pixelmotd.metrics.velocity.VelocityMetricsHandler;
import me.blueslime.slimelib.SlimePlatform;

public abstract class MetricsHandler<T> {

    private final int id;
    private final T main;

    public MetricsHandler(T main, int id) {
        this.main = main;
        this.id   = id;
    }

    public abstract void initialize();

    public int getId() {
        return id;
    }

    public T getMain() {
        return main;
    }

    public static MetricsHandler<?> fromPlatform(SlimePlatform platform, Object main) {
        switch (platform) {
            case SPONGE:
                return new SpongeMetricsHandler(main);
            case VELOCITY:
                return new VelocityMetricsHandler(main);
            case BUKKIT:
                return new BukkitMetricsHandler(main);
            default:
            case BUNGEECORD:
                return new BungeeCordMetricsHandler(main);
        }
    }


}
