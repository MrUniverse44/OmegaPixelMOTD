package me.blueslime.pixelmotd.motd.builder.favicon.platforms;

import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.Icon;
import me.blueslime.pixelmotd.motd.builder.favicon.FaviconModule;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.platforms.BungeeIcon;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

public class BungeeFavicon extends FaviconModule<Plugin, Favicon> {

    public BungeeFavicon(PixelMOTD<Plugin> plugin) {
        super(plugin);
    }

    @Override
    public Icon<Favicon> createIcon(SlimeLogs logs, File icon) {
        return new BungeeIcon(
                logs,
                icon
        );
    }
}
