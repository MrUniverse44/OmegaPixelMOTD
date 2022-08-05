package me.blueslime.pixelmotd.initialization.spigot;

import me.blueslime.pixelmotd.PixelMOTD;
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
