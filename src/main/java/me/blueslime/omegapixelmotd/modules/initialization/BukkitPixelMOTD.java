package me.blueslime.omegapixelmotd.modules.initialization;

import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.omegapixelmotd.modules.metrics.platforms.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPixelMOTD extends JavaPlugin {
    private OmegaPixelMOTD plugin;

    @Override
    public void onEnable() {
        plugin = new OmegaPixelMOTD(this);
        plugin.initialize();

        new Metrics(this, 15577);
    }

    @Override
    public void onDisable() {
        plugin.shutdown();
        plugin = null;
    }
}
