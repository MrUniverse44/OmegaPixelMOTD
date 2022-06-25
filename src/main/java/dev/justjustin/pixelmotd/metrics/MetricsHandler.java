package dev.justjustin.pixelmotd.metrics;

import dev.justjustin.pixelmotd.metrics.bungeecord.BungeeCordMetricsHandler;
import dev.justjustin.pixelmotd.metrics.spigot.SpigotMetricsHandler;
import dev.justjustin.pixelmotd.metrics.sponge.SpongeMetricsHandler;
import dev.justjustin.pixelmotd.metrics.velocity.VelocityMetricsHandler;
import dev.mruniverse.slimelib.SlimePlatform;

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
            case SPIGOT:
                return new SpigotMetricsHandler(main);
            default:
            case BUNGEECORD:
                return new BungeeCordMetricsHandler(main);
        }
    }


}
