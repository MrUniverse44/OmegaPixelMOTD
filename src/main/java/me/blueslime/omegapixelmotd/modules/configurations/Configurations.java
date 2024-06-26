package me.blueslime.omegapixelmotd.modules.configurations;

import me.blueslime.wardenplugin.WardenPlugin;
import me.blueslime.wardenplugin.configuration.ConfigurationHandler;
import me.blueslime.wardenplugin.configuration.file.ConfigurationFile;
import me.blueslime.wardenplugin.modules.list.UniversalModule;

import java.io.File;

public class Configurations extends UniversalModule {

    private ConfigurationHandler countdowns;
    private ConfigurationHandler whitelist;
    private ConfigurationHandler blacklist;
    private ConfigurationHandler settings;
    private ConfigurationHandler motds;

    public Configurations(WardenPlugin<Object> plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        this.motds = getConfigurationProvider().load(
            ConfigurationFile.build(
                new File(getDataFolder(), "motds.yml"),
                Configurations.class.getResourceAsStream("/motds.yml")
            )
        );

        this.settings = getConfigurationProvider().load(
            ConfigurationFile.build(
                new File(getDataFolder(), "settings.yml"),
                Configurations.class.getResourceAsStream("/settings.yml")
            )
        );

        this.countdowns = getConfigurationProvider().load(
            ConfigurationFile.build(
                new File(getDataFolder(), "countdowns.yml"),
                Configurations.class.getResourceAsStream("/countdowns.yml")
            )
        );

        this.whitelist = getConfigurationProvider().load(
            ConfigurationFile.build(
                new File(getDataFolder(), "whitelist.yml"),
                Configurations.class.getResourceAsStream("/whitelist.yml")
            )
        );

        this.blacklist =  getConfigurationProvider().load(
            ConfigurationFile.build(
                new File(getDataFolder(), "blacklist.yml"),
                Configurations.class.getResourceAsStream("/blacklist.yml")
            )
        );
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void reload() {
        initialize();
    }

    public ConfigurationHandler getCountdowns() {
        return countdowns;
    }

    public ConfigurationHandler getWhitelist() {
        return whitelist;
    }

    public ConfigurationHandler getBlacklist() {
        return blacklist;
    }

    public ConfigurationHandler getSettings() {
        return settings;
    }

    public ConfigurationHandler getMotds() {
        return motds;
    }
}
