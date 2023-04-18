package me.blueslime.pixelmotd.motd.builder.favicon.icons.platforms;

import me.blueslime.pixelmotd.motd.builder.favicon.icons.Icon;
import me.blueslime.slimelib.logs.SlimeLogs;
import net.md_5.bungee.api.Favicon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BungeeIcon extends Icon<Favicon> {

    public BungeeIcon(SlimeLogs logs, File icon) {
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

            return Favicon.create(image);
        } catch (IOException exception) {
            error(file, exception);
            return null;
        }
    }
}
