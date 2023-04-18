package me.blueslime.pixelmotd.metrics.bukkit;

import me.blueslime.pixelmotd.metrics.MetricsHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitMetricsHandler extends MetricsHandler<JavaPlugin> {
    public BukkitMetricsHandler(Object main) {
        super((JavaPlugin) main, 15577);
    }

    @Override
    public void initialize() {
        new Metrics(getMain(), getId());
    }
}
