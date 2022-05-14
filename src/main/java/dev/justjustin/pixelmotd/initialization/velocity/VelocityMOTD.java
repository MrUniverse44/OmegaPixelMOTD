package dev.justjustin.pixelmotd.initialization.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.justjustin.pixelmotd.initialization.PixelMOTD;
import dev.mruniverse.slimelib.SlimePlatform;

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

    @Inject
    private ProxyServer server;

    @Inject
    @DataDirectory
    private Path dataDirectory;

    private PixelMOTD<ProxyServer> instance;

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        instance = new PixelMOTD<>(
                SlimePlatform.VELOCITY,
                server,
                new File(dataDirectory.toFile(), "PixelMOTD")
        );
    }

    @Subscribe
    public void onShutdown(ProxyShutdownEvent event) {
        instance.getLoader().shutdown();
    }

}
