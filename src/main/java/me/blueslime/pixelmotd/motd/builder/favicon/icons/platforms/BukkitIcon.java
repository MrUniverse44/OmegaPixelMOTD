package me.blueslime.pixelmotd.motd.builder.favicon.icons.platforms;

import me.blueslime.pixelmotd.motd.builder.favicon.icons.Icon;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import org.bukkit.Bukkit;
import org.bukkit.util.CachedServerIcon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class BukkitIcon extends Icon<CachedServerIcon> {
    public BukkitIcon(SlimeLogs logs, File icon) {
        super(logs, icon);
    }

    @Override
    public CachedServerIcon getFavicon(File file) {
        if (!file.exists()) {
            error(file);
            return null;
        }

        try {
            BufferedImage image = ImageIO.read(file);

            finish(file);

            return Bukkit.loadServerIcon(image);
        } catch (Exception exception) {
            error(file, exception);
            return null;
        }
    }
}
