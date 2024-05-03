package me.blueslime.omegapixelmotd.modules.initialization;

import com.google.inject.Inject;
import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.omegapixelmotd.modules.metrics.platforms.sponge.Metrics2;
import org.spongepowered.api.Server;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.io.File;
import java.nio.file.Path;

@Plugin(
    value = "pixelmotd"
)
public class SpongePixelMOTD {

    @Inject
    private PluginManager pluginManager;

    @Inject
    private PluginContainer container;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path privateConfigDir;

    private OmegaPixelMOTD plugin;

    private final Metrics2 metrics;

    @Inject
    public SpongePixelMOTD(Metrics2.Factory factory) {
        this.metrics = factory.make(15580);
    }

    @Listener
    public void onServerStart(final StartedEngineEvent<Server> event) {
        plugin = new OmegaPixelMOTD(this);

        plugin.getInformation().setDataFolder(new File(privateConfigDir.toFile(), "PixelMOTD"));
        plugin.initialize();
    }

    @Listener
    public void onServerStop(final StoppingEngineEvent<Server> event) {
        plugin.shutdown();
        plugin = null;
    }

}
