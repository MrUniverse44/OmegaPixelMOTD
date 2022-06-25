package dev.justjustin.pixelmotd.metrics.spigot;

import dev.justjustin.pixelmotd.metrics.MetricsHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotMetricsHandler extends MetricsHandler<JavaPlugin> {
    public SpigotMetricsHandler(Object main) {
        super((JavaPlugin) main, 15577);
    }

    @Override
    public void initialize() {
        new Metrics(getMain(), getId());
    }
}
