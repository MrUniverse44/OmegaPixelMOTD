package dev.justjustin.pixelmotd.listener.velocity;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.util.Favicon;
import dev.justjustin.pixelmotd.MotdType;
import dev.justjustin.pixelmotd.initialization.velocity.VelocityMOTD;
import dev.justjustin.pixelmotd.listener.Icon;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class VelocityIcon extends Icon<Favicon> {

    private final SlimeLogs logs;

    public VelocityIcon(SlimeLogs logs, MotdType motdType, File icon) {
        super(logs, motdType, icon);
        this.logs = logs;
    }

    @Override
    public Favicon getFavicon(File file) {
        if (!file.exists()) {
            if (logs != null) {
                logs.error("&cFile doesn't exists: " + file.getName() + " motd-type::" + getType().toString());
            } else {
                log("&8[&9PixelMOTD&8] &cFile doesn't exists: " + file.getName() + " motd-type::" + getType().toString());
            }
            return null;
        }

        try {

            BufferedImage image = ImageIO.read(file);

            if (logs != null) {
                logs.info("&8[&9PixelMOTD&8] &3Icon loaded: &6" + file.getName() + "&a of MotdType &6" + getType().toString());
            } else {
                log("&8[&9PixelMOTD&8] &3Icon loaded: &6" + file.getName() + "&a of MotdType &6" + getType().toString());
            }
            return Favicon.create(image);
        } catch (Exception exception) {
            if (logs != null) {
                logs.error("Can't create favicon: " + file.getName() + ", maybe the icon is not 64x64 or is broken. Showing Exception:", exception);
            } else {
                log("Can't create favicon: " + file.getName() + ", maybe the icon is not 64x64 or is broken. Showing Exception:");
                exception.printStackTrace();
            }
            return null;
        }
    }

    private void log(String message) {
        ConsoleCommandSource source = VelocityMOTD.getInstance().getServer().getConsoleCommandSource();
        source.sendMessage(
                LegacyComponentSerializer.builder().character('&').build().deserialize(message)
        );
    }

    @Override
    public SlimeLogs getLogs() {
        return logs;
    }
}
