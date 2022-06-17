package dev.justjustin.pixelmotd.listener.spigot.packets;

import com.comphenix.protocol.wrappers.WrappedServerPing;
import dev.justjustin.pixelmotd.MotdType;
import dev.justjustin.pixelmotd.listener.Icon;
import dev.mruniverse.slimelib.logs.SlimeLogs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PacketSpigotIcon extends Icon<WrappedServerPing.CompressedImage> {

    public PacketSpigotIcon(SlimeLogs logs, MotdType motdType, File icon) {
        super(logs, motdType, icon);
    }

    @Override
    public WrappedServerPing.CompressedImage getFavicon(File icon) {
        if (!icon.exists()) {
            getLogs().error("File doesn't exists: " + icon.getName() + " motd-type::" + getType().toString());
            return null;
        }

        try {
            BufferedImage image = ImageIO.read(icon);

            getLogs().info("&3Icon loaded: &6" + getName() + "&a of MotdType &6" + getType().toString());

            return WrappedServerPing.CompressedImage.fromPng(image);
        } catch (IOException exception) {
            getLogs().error("Can't create favicon: " + getName() + ", maybe the icon is not 64x64 or is broken. Showing Exception:", exception);
            return null;
        }
    }
}
