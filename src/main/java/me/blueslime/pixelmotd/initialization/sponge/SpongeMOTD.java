package me.blueslime.pixelmotd.initialization.sponge;

import com.google.inject.Inject;
import me.blueslime.pixelmotd.PixelMOTD;
import dev.mruniverse.slimelib.SlimePlatform;
import org.spongepowered.api.Server;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.io.File;
import java.nio.file.Path;

@Plugin(
        value = "pixelmotd"
)
public class SpongeMOTD {

    @Inject
    private PluginManager pluginManager;

    @Inject
    private PluginContainer container;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path privateConfigDir;

    private PixelMOTD<Server> instance;

    @Listener
    public void onServerStart(final StartedEngineEvent<Server> event) {
        instance = new PixelMOTD<>(
                SlimePlatform.SPONGE,
                event.engine(),
                new File(privateConfigDir.toFile(), "PixelMOTD")
        );
    }



}
