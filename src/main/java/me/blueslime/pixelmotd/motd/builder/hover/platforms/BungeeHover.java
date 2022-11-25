package me.blueslime.pixelmotd.motd.builder.hover.platforms;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.hover.HoverController;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Collections;
import java.util.List;

public class BungeeHover extends HoverController<Plugin, ServerPing.PlayerInfo> {

    public BungeeHover(PixelMOTD<Plugin> plugin) {
        super(plugin);
    }

    @Override
    public List<ServerPing.PlayerInfo> fromList(List<String> lines) {
        //TODO: This will be worked in the future.
        return Collections.emptyList();
    }
}
