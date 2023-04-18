package me.blueslime.pixelmotd.motd.builder.favicon.icons.platforms;

import me.blueslime.pixelmotd.motd.builder.favicon.icons.Icon;
import me.blueslime.slimelib.logs.SlimeLogs;
import org.spongepowered.api.network.status.Favicon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class SpongeIcon extends Icon<Favicon> {
    public SpongeIcon(SlimeLogs logs, File icon) {
        super(logs, icon);
    }

    @Override
    public Favicon getFavicon(File file) {
        if (!file.exists()) {
            error(file);
            return null;
        }

        try {
            BufferedImage image = ImageIO.read(file);

            finish(file);

            return Favicon.load(image);
        } catch (Exception exception) {
            error(file, exception);
            return null;
        }
    }
}
