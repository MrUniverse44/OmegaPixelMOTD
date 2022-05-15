package dev.justjustin.pixelmotd.initialization.bungeecord;

import dev.justjustin.pixelmotd.PixelMOTD;
import dev.mruniverse.slimelib.SlimePlatform;
import net.md_5.bungee.api.plugin.Plugin;

@SuppressWarnings("unused")
public final class BungeeMOTD extends Plugin {

    private PixelMOTD<Plugin> instance;

    @Override
    public void onEnable() {
        instance = new PixelMOTD<>(
                SlimePlatform.BUNGEECORD,
                this,
                getDataFolder()
        );
    }

    @Override
    public void onDisable() {
        instance.getLoader().shutdown();
    }
}
