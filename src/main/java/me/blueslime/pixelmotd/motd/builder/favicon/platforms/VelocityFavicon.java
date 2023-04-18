package me.blueslime.pixelmotd.motd.builder.favicon.platforms;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.util.Favicon;
import me.blueslime.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.Icon;
import me.blueslime.pixelmotd.motd.builder.favicon.FaviconModule;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.platforms.VelocityIcon;

import java.io.File;

public class VelocityFavicon extends FaviconModule<ProxyServer, Favicon> {

    public VelocityFavicon(PixelMOTD<ProxyServer> plugin) {
        super(plugin);
    }

    @Override
    public Icon<Favicon> createIcon(SlimeLogs logs, File icon) {
        return new VelocityIcon(
                getLogs(),
                icon
        );
    }
}
