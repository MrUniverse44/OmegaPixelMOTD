package me.blueslime.pixelmotd.listener.type;

import com.velocitypowered.api.proxy.ProxyServer;
import me.blueslime.slimelib.file.configuration.ConfigurationHandler;
import me.blueslime.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.Configuration;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.exception.NotFoundLanguageException;
import me.blueslime.pixelmotd.initialization.velocity.VelocityMOTD;
import me.blueslime.pixelmotd.listener.PluginListener;
import me.blueslime.pixelmotd.players.PlayerDatabase;
import me.blueslime.pixelmotd.players.PlayerHandler;
import me.blueslime.pixelmotd.servers.ServerHandler;
import me.blueslime.pixelmotd.utils.ListType;
import me.blueslime.pixelmotd.utils.list.PluginList;
import me.blueslime.pixelmotd.utils.placeholders.PluginPlaceholders;

import java.util.UUID;

public abstract class VelocityPluginListener implements PluginListener<ProxyServer> {
    private final PixelMOTD<ProxyServer> plugin;

    @SuppressWarnings("unchecked")
    public VelocityPluginListener(PixelMOTD<?> plugin) {
        this.plugin = (PixelMOTD<ProxyServer>) plugin;
    }

    public void register() {
        plugin.getPlugin().getEventManager().register(
                VelocityMOTD.getInstance(),
                this
        );
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
    public PlayerHandler getPlayerHandler() {
        return plugin.getPlayerHandler();
    }

    @Override
    public ServerHandler getServerHandler() {
        return plugin.getServerHandler();
    }

    @Override
    public PluginPlaceholders getPlaceholders() {
        return plugin.getPlaceholders();
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
    public PixelMOTD<ProxyServer> getBasePlugin() {
        return plugin;
    }
}
