package dev.justjustin.pixelmotd.listener.spigot;

import dev.justjustin.pixelmotd.MotdType;
import dev.justjustin.pixelmotd.listener.Icon;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import org.bukkit.Bukkit;
import org.bukkit.util.CachedServerIcon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class SpigotIcon extends Icon<CachedServerIcon> {
    public SpigotIcon(SlimeLogs logs, MotdType motdType, File icon) {
        super(logs, motdType, icon);
    }

    @Override
    public CachedServerIcon getFavicon(File file) {
        if (!file.exists()) {
            getLogs().error("File doesn't exists: " + file.getName() + " motd-type::" + getType().toString());
            return null;
        }

        try {
            BufferedImage image = ImageIO.read(file);

            getLogs().info("&aIcon loaded: &6" + getName() + "&a of MotdType &6" + getType().toString());

            return Bukkit.loadServerIcon(image);
        } catch (Exception exception) {
            getLogs().error("Can't create favicon: " + getName() + ", maybe the icon is not 64x64 or is broken. Showing Exception:", exception);

            return null;
        }
    }
}
