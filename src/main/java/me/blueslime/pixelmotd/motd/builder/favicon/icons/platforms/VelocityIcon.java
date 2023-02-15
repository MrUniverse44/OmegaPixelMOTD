package me.blueslime.pixelmotd.motd.builder.favicon.icons.platforms;

import com.velocitypowered.api.util.Favicon;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.Icon;
import dev.mruniverse.slimelib.logs.SlimeLogs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class VelocityIcon extends Icon<Favicon> {

    public VelocityIcon(SlimeLogs logs, File icon) {
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
        } catch (Exception exception) {
            error(file, exception);
            return null;
        }
    }
}
