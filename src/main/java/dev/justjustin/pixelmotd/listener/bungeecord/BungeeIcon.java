package dev.justjustin.pixelmotd.listener.bungeecord;

import dev.justjustin.pixelmotd.MotdType;
import dev.justjustin.pixelmotd.listener.Icon;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import net.md_5.bungee.api.Favicon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BungeeIcon extends Icon<Favicon> {
    public BungeeIcon(SlimeLogs logs, MotdType motdType, File icon) {
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

            getLogs().info("&aIcon loaded: &6" + getName() + "&a of MotdType &6" + getType().toString());

            return Favicon.create(image);
        } catch (IOException exception) {
            getLogs().error("Can't create favicon: " + getName() + ", maybe the icon is not 64x64 or is broken. Showing Exception:", exception);
            return null;
        }
    }
}
