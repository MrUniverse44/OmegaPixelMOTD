package me.blueslime.pixelmotd.motd.builder.favicon.platforms;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.util.Favicon;
import me.blueslime.pixelmotd.motd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.favicon.FaviconController;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.Icon;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.platforms.VelocityIcon;

import java.io.File;

public class VelocityFavicon extends FaviconController<ProxyServer, Favicon> {

    public VelocityFavicon(PixelMOTD<ProxyServer> plugin, SlimeLogs logs) {
        super(plugin, logs);
    }

    @Override
    public Icon<Favicon> createIcon(MotdType motdType, File icon) {
        return new VelocityIcon(
                getLogs(),
                motdType,
                icon
        );
    }
}
