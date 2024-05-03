package me.blueslime.omegapixelmotd.modules.commands.interfaces;

import me.blueslime.omegapixelmotd.modules.initialization.BungeecordPixelMOTD;
import me.blueslime.wardenplugin.WardenPlugin;
import me.blueslime.wardenplugin.configuration.ConfigurationProvider;
import me.blueslime.wardenplugin.logs.WardenLogs;
import me.blueslime.wardenplugin.modules.PluginModule;
import me.blueslime.wardenplugin.platform.Platform;
import net.md_5.bungee.api.plugin.Command;

import java.io.File;

public abstract class BungeecordCommandInterface extends Command implements PluginModule {
    private final WardenPlugin<BungeecordPixelMOTD> plugin;

    /**
     * Construct a new command with no permissions or aliases.
     *
     * @param name the name of this command
     */
    public BungeecordCommandInterface(WardenPlugin<BungeecordPixelMOTD> plugin, String name) {
        super(name);
        this.plugin = plugin;
    }

    public ConfigurationProvider getConfigurationProvider() {
        return plugin.getConfigurationProvider();
    }

    public WardenLogs getLogs() {
        return plugin.getLogs();
    }

    public Platform getInformation() {
        return plugin.getInformation();
    }

    public File getDataFolder() {
        return plugin.getDataFolder();
    }

    public WardenPlugin<BungeecordPixelMOTD> getPlugin() {
        return plugin;
    }
}
