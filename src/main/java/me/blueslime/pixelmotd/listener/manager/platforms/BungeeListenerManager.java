package me.blueslime.pixelmotd.listener.manager.platforms;

import me.blueslime.pixelmotd.listener.manager.ListenerManager;
import me.blueslime.pixelmotd.PixelMOTD;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class BungeeListenerManager extends ListenerManager<Plugin> {

    public BungeeListenerManager(PixelMOTD<?> plugin, SlimeLogs logs) {
        super(plugin, logs);
    }

    @Override
    public void register() {
        final Plugin plugin = getPlugin().getPlugin();

        PluginManager manager = plugin.getProxy().getPluginManager();

    }

    @Override
    public void update() {
        //TODO: Update
    }
}
