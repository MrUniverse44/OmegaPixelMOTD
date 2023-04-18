package me.blueslime.pixelmotd.motd.builder.favicon.icons.platforms;

import com.comphenix.protocol.wrappers.WrappedServerPing;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.Icon;
import me.blueslime.slimelib.logs.SlimeLogs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ProtocolIcon extends Icon<WrappedServerPing.CompressedImage> {

    public ProtocolIcon(SlimeLogs logs, File icon) {
        super(logs, icon);
    }

    @Override
    public WrappedServerPing.CompressedImage getFavicon(File icon) {
        if (!icon.exists()) {
            error(icon);
            return null;
        }

        try {
            BufferedImage image = ImageIO.read(icon);

            finish(icon);

            return WrappedServerPing.CompressedImage.fromPng(image);
        } catch (IOException exception) {
            error(icon, exception);
            return null;
        }
    }
}
