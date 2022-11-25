package me.blueslime.pixelmotd.motd.builder.favicon.platforms;

import me.blueslime.pixelmotd.motd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.favicon.FaviconController;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.Icon;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.platforms.BukkitIcon;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.CachedServerIcon;

import java.io.File;

public class BukkitFavicon extends FaviconController<JavaPlugin, CachedServerIcon> {

    public BukkitFavicon(PixelMOTD<JavaPlugin> plugin, SlimeLogs logs) {
        super(plugin, logs);
    }

    @Override
    public Icon<CachedServerIcon> createIcon(MotdType motdType, File icon) {
        return new BukkitIcon(
                getLogs(),
                motdType,
                icon
        );
    }
}
