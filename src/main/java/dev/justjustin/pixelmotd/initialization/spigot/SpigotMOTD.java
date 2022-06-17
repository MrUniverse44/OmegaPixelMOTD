package dev.justjustin.pixelmotd.initialization.spigot;

import dev.justjustin.pixelmotd.PixelMOTD;
import dev.mruniverse.slimelib.SlimePlatform;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class SpigotMOTD extends JavaPlugin {

    private PixelMOTD<JavaPlugin> instance;

    @Override
    public void onEnable() {
        instance = new PixelMOTD<>(
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
