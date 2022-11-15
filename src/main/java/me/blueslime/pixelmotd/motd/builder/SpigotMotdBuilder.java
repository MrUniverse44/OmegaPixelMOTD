package me.blueslime.pixelmotd.motd.builder;

import me.blueslime.pixelmotd.motd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.icons.Icon;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.motd.builder.icons.spigot.SpigotIcon;
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
