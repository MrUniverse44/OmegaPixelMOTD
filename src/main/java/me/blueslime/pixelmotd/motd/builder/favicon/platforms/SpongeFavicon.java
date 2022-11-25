package me.blueslime.pixelmotd.motd.builder.favicon.platforms;

import me.blueslime.pixelmotd.motd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.favicon.FaviconController;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.Icon;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.platforms.SpongeIcon;
import org.spongepowered.api.Server;
import org.spongepowered.api.network.status.Favicon;

import java.io.File;

public class SpongeFavicon extends FaviconController<Server, Favicon> {

    public SpongeFavicon(PixelMOTD<Server> plugin, SlimeLogs logs) {
        super(plugin, logs);
    }

    @Override
    public Icon<Favicon> createIcon(MotdType motdType, File icon) {
        return new SpongeIcon(
                getLogs(),
                motdType,
                icon
        );
    }
}
