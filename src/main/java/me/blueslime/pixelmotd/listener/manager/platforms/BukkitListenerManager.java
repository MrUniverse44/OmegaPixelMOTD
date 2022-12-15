package me.blueslime.pixelmotd.listener.manager.platforms;

import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.manager.ListenerManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitListenerManager extends ListenerManager<JavaPlugin> {
    public BukkitListenerManager(PixelMOTD<?> plugin, SlimeLogs logs) {
        super(plugin, logs);
    }

    @Override
    public void register() {

    }

    @Override
    public void update() {

    }
    //TODO: This will work in the future.
}
