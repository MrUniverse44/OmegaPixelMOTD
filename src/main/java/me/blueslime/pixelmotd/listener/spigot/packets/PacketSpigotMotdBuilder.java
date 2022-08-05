package me.blueslime.pixelmotd.listener.spigot.packets;

import com.comphenix.protocol.wrappers.WrappedServerPing;
import me.blueslime.pixelmotd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.Icon;
import me.blueslime.pixelmotd.listener.MotdBuilder;
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