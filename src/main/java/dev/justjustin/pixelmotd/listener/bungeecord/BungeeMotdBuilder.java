package dev.justjustin.pixelmotd.listener.bungeecord;

import dev.justjustin.pixelmotd.MotdType;
import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.listener.Icon;
import dev.justjustin.pixelmotd.listener.MotdBuilder;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

public class BungeeMotdBuilder extends MotdBuilder<Plugin, Favicon> {

    @SuppressWarnings("unchecked")
    public <T> BungeeMotdBuilder(PixelMOTD<T> plugin, SlimeLogs logs) {
        super((PixelMOTD<Plugin>) plugin, logs);
    }

    @Override
    public Icon<Favicon> createIcon(MotdType motdType, File icon) {
        return new BungeeIcon(
                getLogs(),
                motdType,
                icon
        );
    }
}
