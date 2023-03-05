package me.blueslime.pixelmotd.motd.builder.hover.platforms;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.hover.HoverModule;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProtocolHover extends HoverModule<WrappedGameProfile> {
    public ProtocolHover(PixelMOTD<?> plugin) {
        super(plugin);
    }

    @Override
    public List<WrappedGameProfile> generate(List<String> lineList, String user, int online, int max) {
        final List<WrappedGameProfile> sample = new ArrayList<>();
        final UUID uuid = new UUID(0, 0);

        List<String> lines;

        if (hasPlayers()) {
            lines = getExtras().replaceHoverLine(
                    lineList
            );
        } else {
            lines = lineList;
        }

        for (String line : lines) {
            sample.add(
                    new WrappedGameProfile(
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
    public WrappedGameProfile[] convert(List<WrappedGameProfile> list) {
        return list.toArray(new WrappedGameProfile[0]);
    }
}
