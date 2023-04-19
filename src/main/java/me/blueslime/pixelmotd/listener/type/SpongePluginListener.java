package me.blueslime.pixelmotd.listener.type;

import com.google.inject.Inject;
import me.blueslime.pixelmotd.listener.sponge.SpongeListener;
import me.blueslime.slimelib.file.configuration.ConfigurationHandler;
import me.blueslime.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.Configuration;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.exception.NotFoundLanguageException;
import me.blueslime.pixelmotd.listener.PluginListener;
import me.blueslime.pixelmotd.players.PlayerDatabase;
import me.blueslime.pixelmotd.players.PlayerHandler;
import me.blueslime.pixelmotd.servers.ServerHandler;
import me.blueslime.pixelmotd.utils.ListType;
import me.blueslime.pixelmotd.utils.list.PluginList;
import me.blueslime.pixelmotd.utils.placeholders.PluginPlaceholders;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.plugin.PluginContainer;

import java.util.UUID;

public abstract class SpongePluginListener implements PluginListener<Server> {
    private final PixelMOTD<Server> plugin;

    @Inject
    private PluginContainer container;

    private final SpongeListener listener;

    @SuppressWarnings("unchecked")
    public SpongePluginListener(PixelMOTD<?> plugin, SpongeListener listener) {
        this.listener = listener;
        this.plugin = (PixelMOTD<Server>) plugin;
    }

    public void register() {
        Sponge.eventManager().registerListeners(container, this);
    }

    public boolean checkPlayer(ListType listType, String path, String username) {
        if (path.equals("global")) {
            return plugin.getConfiguration(listType.getFile()).getStringList("players.by-name").contains(username);
        }
        return plugin.getConfiguration(listType.getFile()).getStringList(path + ".players.by-name").contains(username);
    }

    public boolean checkUUID(ListType listType, String path, UUID uniqueId) {
        if (path.equals("global")) {
            return plugin.getConfiguration(listType.getFile()).getStringList("players.by-uuid").contains(uniqueId.toString());
        }
        return plugin.getConfiguration(listType.getFile()).getStringList(path + ".players.by-uuid").contains(uniqueId.toString());
    }

    @Override
    public ConfigurationHandler getWhitelist() {
        return plugin.getConfiguration(Configuration.WHITELIST);
    }

    @Override
    public ConfigurationHandler getBlacklist() {
        return plugin.getConfiguration(Configuration.BLACKLIST);
    }

    @Override
    public ConfigurationHandler getSettings() {
        return plugin.getSettings();
    }

    @Override
    public ConfigurationHandler getMessages() throws NotFoundLanguageException {
        return plugin.getMessages();
    }

    @Override
    public ConfigurationHandler getConfiguration(Configuration configuration) {
        return plugin.getConfiguration(configuration);
    }

    @Override
    public SlimeLogs getLogs() {
        return plugin.getLogs();
    }

    @Override
    public PluginPlaceholders getPlaceholders() {
        return plugin.getPlaceholders();
    }

    @Override
    public PlayerHandler getPlayerHandler() {
        return plugin.getPlayerHandler();
    }

    @Override
    public ServerHandler getServerHandler() {
        return plugin.getServerHandler();
    }

    @Override
    public PluginList getSerializer() {
        return PluginList.fromPlatform(plugin.getServerType());
    }

    @Override
    public PlayerDatabase getPlayerDatabase() {
        return plugin.getLoader().getDatabase();
    }

    @Override
    public String getName() {
        return listener.toString();
    }

    @Override
    public PixelMOTD<Server> getBasePlugin() {
        return plugin;
    }
}
