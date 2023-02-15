package me.blueslime.pixelmotd.motd.builder.hover.platforms;

import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.hover.EmptyPlayerInfo;
import me.blueslime.pixelmotd.motd.builder.hover.HoverModule;

import java.util.Collections;
import java.util.List;

public class BukkitHover extends HoverModule<EmptyPlayerInfo> {
    public BukkitHover(PixelMOTD<?> plugin) {
        super(plugin);
    }

    @Override
    public List<EmptyPlayerInfo> generate(ConfigurationHandler configuration, String path, String user, int online, int max) {
        return Collections.emptyList();
    }

    @Override
    public EmptyPlayerInfo[] convert(List<EmptyPlayerInfo> list) {
        return new EmptyPlayerInfo[0];
    }
}
