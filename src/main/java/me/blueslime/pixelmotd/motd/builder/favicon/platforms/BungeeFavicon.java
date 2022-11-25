package me.blueslime.pixelmotd.motd.builder.favicon.platforms;

import me.blueslime.pixelmotd.motd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.favicon.FaviconController;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.Icon;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.platforms.BungeeIcon;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

public class BungeeFavicon extends FaviconController<Plugin, Favicon> {

    public BungeeFavicon(PixelMOTD<Plugin> plugin, SlimeLogs logs) {
        super(plugin, logs);
    }

    @Override
    public Icon<Favicon> createIcon(MotdType motdType, File icon) {
        return new BungeeIcon(
                super.getLogs(),
                motdType,
                icon
        );
    }
}
