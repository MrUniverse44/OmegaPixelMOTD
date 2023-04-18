package me.blueslime.pixelmotd.motd.builder.favicon.platforms;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.Icon;
import me.blueslime.pixelmotd.motd.builder.favicon.FaviconModule;
import me.blueslime.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.platforms.SpongeIcon;
import org.spongepowered.api.Server;
import org.spongepowered.api.network.status.Favicon;

import java.io.File;

public class SpongeFavicon extends FaviconModule<Server, Favicon> {

    public SpongeFavicon(PixelMOTD<Server> plugin) {
        super(plugin);
    }

    @Override
    public Icon<Favicon> createIcon(SlimeLogs logs, File icon) {
        return new SpongeIcon(
                logs,
                icon
        );
    }
}
