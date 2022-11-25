package me.blueslime.pixelmotd.motd.builder.hover.platforms;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.hover.HoverController;
import org.bukkit.Server;
import org.spongepowered.api.profile.GameProfile;

import java.util.Collections;
import java.util.List;

public class SpongeHover extends HoverController<Server, GameProfile> {

    public SpongeHover(PixelMOTD<Server> plugin) {
        super(plugin);
    }

    @Override
    public List<GameProfile> fromList(List<String> lines) {
        //TODO: This will be worked in the future.
        return Collections.emptyList();
    }
}
