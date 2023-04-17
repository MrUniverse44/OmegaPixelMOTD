package me.blueslime.pixelmotd.initialization.bukkit;

import me.blueslime.pixelmotd.PixelMOTD;
import dev.mruniverse.slimelib.SlimePlatform;
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
    }

    @Override
    public void onDisable() {
        instance.getLoader().shutdown();
    }

}
