package me.blueslime.pixelmotd.motd.builder.hover.platforms;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.hover.HoverController;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

public class PaperHover extends HoverController<JavaPlugin, PlayerProfile> {
    public PaperHover(PixelMOTD<JavaPlugin> plugin) {
        super(plugin);
    }

    @Override
    public List<PlayerProfile> fromList(List<String> lines) {
        //TODO: This will be worked in the future
        return Collections.emptyList();
    }
}
