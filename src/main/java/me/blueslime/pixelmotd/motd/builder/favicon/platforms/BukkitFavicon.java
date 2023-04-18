package me.blueslime.pixelmotd.motd.builder.favicon.platforms;

import me.blueslime.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.Icon;
import me.blueslime.pixelmotd.motd.builder.favicon.FaviconModule;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.platforms.BukkitIcon;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.CachedServerIcon;

import java.io.File;

public class BukkitFavicon extends FaviconModule<JavaPlugin, CachedServerIcon> {

    public BukkitFavicon(PixelMOTD<JavaPlugin> plugin) {
        super(plugin);
    }

    @Override
    public Icon<CachedServerIcon> createIcon(SlimeLogs logs, File icon) {
        return new BukkitIcon(
                logs,
                icon
        );
    }
}
