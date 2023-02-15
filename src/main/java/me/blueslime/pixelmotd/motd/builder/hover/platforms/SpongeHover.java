package me.blueslime.pixelmotd.motd.builder.hover.platforms;

import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.file.configuration.TextDecoration;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.hover.HoverModule;
import org.spongepowered.api.profile.GameProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpongeHover extends HoverModule<GameProfile> {
    public SpongeHover(PixelMOTD<?> plugin) {
        super(plugin);
    }

    @Override
    public List<GameProfile> generate(ConfigurationHandler configuration, String path, String user, int online, int max) {
        final List<GameProfile> sample = new ArrayList<>();
        final UUID uuid = new UUID(0, 0);

        List<String> lines;

        if (hasPlayers()) {
            lines = getExtras().replaceHoverLine(
                    configuration.getStringList(TextDecoration.LEGACY, path + "hover.lines")
            );
        } else {
            lines = configuration.getStringList(TextDecoration.LEGACY, path + "hover.lines");
        }

        for (String line : lines) {
            sample.add(
                    GameProfile.of(
                            uuid,
                            getExtras().replace(
                                    line, online, max, user
                            )
                    )
            );
        }

        return sample;
    }

    @Override
    public GameProfile[] convert(List<GameProfile> list) {
        return list.toArray(new GameProfile[0]);
    }
}
