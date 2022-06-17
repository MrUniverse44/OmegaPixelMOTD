package dev.justjustin.pixelmotd.listener.sponge;

import dev.justjustin.pixelmotd.MotdType;
import dev.justjustin.pixelmotd.listener.Icon;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import org.spongepowered.api.network.status.Favicon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class SpongeIcon extends Icon<Favicon> {
    public SpongeIcon(SlimeLogs logs, MotdType motdType, File icon) {
        super(logs, motdType, icon);
    }

    @Override
    public Favicon getFavicon(File file) {
        if (!file.exists()) {
            getLogs().error("File doesn't exists: " + file.getName() + " motd-type::" + getType().toString());
            return null;
        }

        try {
            BufferedImage image = ImageIO.read(file);

            getLogs().info("&3Icon loaded: &6" + file.getName() + "&3 of MotdType &6" + getType().toString());

            return Favicon.load(image);
        } catch (Exception exception) {
            getLogs().error("Can't create favicon: " + file.getName() + ", maybe the icon is not 64x64 or is broken. Showing Exception:", exception);

            return null;
        }
    }
}
