package me.blueslime.omegapixelmotd.modules.initialization;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import lombok.Getter;
import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.omegapixelmotd.modules.metrics.platforms.velocity.Metrics;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;

@SuppressWarnings("unused")
@Plugin(
        id = "pixelmotd",
        name = "PixelMOTD",
        version = "9.4.1-SNAPSHOT",
        description = "Simple Motd Plugin",
        url = "https://github.com/MrUniverse44/XPixelMotds4",
        authors = { "JustJustin" }
)
public class VelocityPixelMOTD {
    private OmegaPixelMOTD plugin;

    @Getter
    @Inject
    private ProxyServer server;

    @Inject
    @DataDirectory
    private Path dataDirectory;

    @Getter
    @Inject
    private Logger logger;

    @Inject
    private Metrics.Factory metricsFactory;

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        File directory = dataDirectory.getParent().toFile();

        plugin = new OmegaPixelMOTD(server);
        plugin.getInformation().setDataFolder(new File(directory, "PixelMOTD"));

        initMetrics(15579);

        plugin.initialize();
    }

    @Subscribe
    public void onShutdown(ProxyShutdownEvent event) {
        plugin.shutdown();
    }

    public void initMetrics(int id) {
        Metrics metrics = metricsFactory.make(this, id);
    }
}
