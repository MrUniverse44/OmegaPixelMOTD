package me.blueslime.omegapixelmotd.modules.initialization;

import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.omegapixelmotd.modules.metrics.platforms.bungeecord.Metrics;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeecordPixelMOTD extends Plugin {
    private OmegaPixelMOTD plugin;

    @Override
    public void onEnable() {
        plugin = new OmegaPixelMOTD(this);
        plugin.initialize();

        new Metrics(this, 15578);
    }

    @Override
    public void onDisable() {
        plugin.shutdown();
        plugin = null;
    }
}
