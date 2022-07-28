package dev.justjustin.pixelmotd.initialization.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.SlimeFile;
import dev.justjustin.pixelmotd.listener.velocity.VelocityListenerManager;
import dev.justjustin.pixelmotd.metrics.velocity.Metrics;
import dev.justjustin.pixelmotd.status.StatusChecker;
import dev.justjustin.pixelmotd.status.VelocityServerStatusChecker;
import dev.mruniverse.slimelib.SlimePlatform;

import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;

@SuppressWarnings("unused")
@Plugin(
        id = "pixelmotd",
        name = "PixelMOTD",
        version = "9.3.0-SNAPSHOT",
        description = "Simple Motd Plugin",
        url = "https://github.com/MrUniverse44/XPixelMotds4",
        authors = { "JustJustin" }
)
public class VelocityMOTD {

    private static VelocityMOTD classInstance;

    private VelocityServerStatusChecker checker = null;

    private PixelMOTD<ProxyServer> instance;
    @Inject
    private ProxyServer server;

    @Inject
    @DataDirectory
    private Path dataDirectory;

    @Inject
    private Logger logger;

    @Inject
    private Metrics.Factory metricsFactory;

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {

        classInstance = this;

        File directory = dataDirectory.getParent().toFile();

        instance = new PixelMOTD<>(
                SlimePlatform.VELOCITY,
                server,
                new File(directory, "PixelMOTD")
        );

        if (instance.getLoader().getFiles().getControl(SlimeFile.SETTINGS).getStatus("settings.server-status.toggle")) {
            checker = new VelocityServerStatusChecker(instance);
        }

        VelocityListenerManager manager = (VelocityListenerManager) instance.getListenerManager();

        manager.register(this);
    }

    @Subscribe
    public void onShutdown(ProxyShutdownEvent event) {
        instance.getLoader().shutdown();
    }

    public ProxyServer getServer() {
        return server;
    }

    public StatusChecker getChecker() {
        return checker;
    }

    public Logger getLogger() {
        return logger;
    }

    public void initMetrics(int id) {
        Metrics metrics = metricsFactory.make(this, id);
    }

    public static VelocityMOTD getInstance() {
        return classInstance;
    }

}
