package me.blueslime.pixelmotd.motd.builder.hover.platforms;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.hover.HoverController;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

public class ProtocolHover extends HoverController<JavaPlugin, WrappedGameProfile> {
    public ProtocolHover(PixelMOTD<JavaPlugin> plugin) {
        super(plugin);
    }

    @Override
    public List<WrappedGameProfile> fromList(List<String> lines) {
        //TODO: This will be worked in the future
        return Collections.emptyList();
    }
}
