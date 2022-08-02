package dev.justjustin.pixelmotd.initialization.bungeecord;

import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.SlimeFile;
import dev.justjustin.pixelmotd.status.ServerStatusChecker;
import dev.justjustin.pixelmotd.status.StatusChecker;
import dev.mruniverse.slimelib.SlimePlatform;
import net.md_5.bungee.api.plugin.Plugin;

@SuppressWarnings("unused")
public final class BungeeMOTD extends Plugin {

    private static BungeeMOTD bungeeMOTD;

    private ServerStatusChecker checker = null;

    private PixelMOTD<Plugin> instance;

    @Override
    public void onEnable() {
        bungeeMOTD = this;

        instance = new PixelMOTD<>(
                SlimePlatform.BUNGEECORD,
                this,
                getDataFolder()
        );

        if (instance.getConfigurationHandler(SlimeFile.SETTINGS).getStatus("settings.server-status.toggle")) {
            checker = new ServerStatusChecker(instance);
        }
    }

    @Override
    public void onDisable() {
        instance.getLoader().shutdown();
    }

    public static BungeeMOTD getInstance() {
        return bungeeMOTD;
    }

    public StatusChecker getChecker() {
        return checker;
    }

    public PixelMOTD<Plugin> getPlugin() {
        return instance;
     }
}
