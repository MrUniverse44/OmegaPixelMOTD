package me.blueslime.pixelmotd.listener.spigot;

import me.blueslime.pixelmotd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.Icon;
import me.blueslime.pixelmotd.listener.MotdBuilder;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.CachedServerIcon;

import java.io.File;

public class SpigotMotdBuilder extends MotdBuilder<JavaPlugin, CachedServerIcon> {

    public SpigotMotdBuilder(PixelMOTD<JavaPlugin> plugin, SlimeLogs logs) {
        super(plugin, logs);
    }

    @Override
    public Icon<CachedServerIcon> createIcon(MotdType motdType, File icon) {
        return new SpigotIcon(
                getLogs(),
                motdType,
                icon
        );
    }
}
