package me.blueslime.pixelmotd.motd.builder.favicon.platforms;

import com.comphenix.protocol.wrappers.WrappedServerPing;
import me.blueslime.pixelmotd.motd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.favicon.FaviconController;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.Icon;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.platforms.ProtocolIcon;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ProtocolFavicon extends FaviconController<JavaPlugin, WrappedServerPing.CompressedImage> {

    public ProtocolFavicon(PixelMOTD<JavaPlugin> plugin, SlimeLogs logs) {
        super(plugin, logs);
    }

    @Override
    public Icon<WrappedServerPing.CompressedImage> createIcon(MotdType motdType, File icon) {
        return new ProtocolIcon(
                getLogs(),
                motdType,
                icon
        );
    }
}