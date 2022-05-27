package dev.justjustin.pixelmotd.listener.spigot.packets;

import com.comphenix.protocol.wrappers.WrappedServerPing;
import dev.justjustin.pixelmotd.MotdType;
import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.listener.Icon;
import dev.justjustin.pixelmotd.listener.MotdBuilder;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class PacketSpigotMotdBuilder extends MotdBuilder<JavaPlugin, WrappedServerPing.CompressedImage> {

    public PacketSpigotMotdBuilder(PixelMOTD<JavaPlugin> plugin, SlimeLogs logs) {
        super(plugin, logs);
    }

    @Override
    public Icon<WrappedServerPing.CompressedImage> createIcon(MotdType motdType, File icon) {
        return new PacketSpigotIcon(
                getLogs(),
                motdType,
                icon
        );
    }
}