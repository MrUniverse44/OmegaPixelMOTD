package me.blueslime.pixelmotd.initialization.bukkit;

import me.blueslime.pixelmotd.PixelMOTD;
import dev.mruniverse.slimelib.SlimePlatform;
import me.blueslime.pixelmotd.motd.manager.platforms.BukkitListenerManager;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class BukkitMOTD extends JavaPlugin {

    private PixelMOTD<JavaPlugin> instance;

    @Override
    public void onEnable() {
        this.instance = new PixelMOTD<>(
                SlimePlatform.SPIGOT,
                this,
                getDataFolder()
        );

        this.instance.initialize(
                new BukkitListenerManager(
                        instance
                )
        );

        this.instance.getListenerManager().register();
    }

    @Override
    public void onDisable() {
        instance.getLoader().shutdown();
    }

}
