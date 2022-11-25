package me.blueslime.pixelmotd.motd.builder.favicon.icons.platforms;

import com.velocitypowered.api.util.Favicon;
import me.blueslime.pixelmotd.motd.MotdType;
import me.blueslime.pixelmotd.motd.builder.favicon.icons.Icon;
import dev.mruniverse.slimelib.logs.SlimeLogs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class VelocityIcon extends Icon<Favicon> {

    public VelocityIcon(SlimeLogs logs, MotdType motdType, File icon) {
        super(logs, motdType, icon);
    }

    @Override
    public Favicon getFavicon(File file) {
        if (!file.exists()) {
            getLogs().error("&cFile doesn't exists: " + file.getName() + " motd-type::" + getType().toString());
            return null;
        }

        try {

            BufferedImage image = ImageIO.read(file);

            getLogs().info("&3Icon loaded: &6" + file.getName() + "&a of MotdType &6" + getType().toString());

            return Favicon.create(image);
        } catch (Exception exception) {
            getLogs().error("Can't create favicon: " + file.getName() + ", maybe the icon is not 64x64 or is broken. Showing Exception:", exception);
            return null;
        }
    }
}