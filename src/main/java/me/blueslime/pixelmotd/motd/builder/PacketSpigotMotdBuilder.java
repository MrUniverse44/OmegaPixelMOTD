package me.blueslime.pixelmotd.motd.builder;

import com.comphenix.protocol.wrappers.WrappedServerPing;
import me.blueslime.pixelmotd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.icons.Icon;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.motd.builder.icons.spigot.PacketSpigotIcon;
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