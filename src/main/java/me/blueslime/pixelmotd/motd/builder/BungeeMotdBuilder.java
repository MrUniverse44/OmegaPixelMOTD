package me.blueslime.pixelmotd.motd.builder;

import me.blueslime.pixelmotd.motd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.icons.Icon;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.motd.builder.icons.bungeecord.BungeeIcon;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

public class BungeeMotdBuilder extends MotdBuilder<Plugin, Favicon> {

    public BungeeMotdBuilder(PixelMOTD<Plugin> plugin, SlimeLogs logs) {
        super(plugin, logs);
    }

    @Override
    public Icon<Favicon> createIcon(MotdType motdType, File icon) {
        return new BungeeIcon(
                super.getLogs(),
                motdType,
                icon
        );
    }
}
