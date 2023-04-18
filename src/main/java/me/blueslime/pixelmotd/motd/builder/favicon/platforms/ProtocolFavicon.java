package me.blueslime.pixelmotd.motd.builder.favicon.platforms;

import com.comphenix.protocol.wrappers.WrappedServerPing;
import me.blueslime.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.Icon;
import me.blueslime.pixelmotd.motd.builder.favicon.FaviconModule;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.platforms.ProtocolIcon;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ProtocolFavicon extends FaviconModule<JavaPlugin, WrappedServerPing.CompressedImage> {

    public ProtocolFavicon(PixelMOTD<JavaPlugin> plugin) {
        super(plugin);
    }

    @Override
    public Icon<WrappedServerPing.CompressedImage> createIcon(SlimeLogs logs, File icon) {
        return new ProtocolIcon(
                logs,
                icon
        );
    }
}