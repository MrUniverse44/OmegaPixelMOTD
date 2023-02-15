package me.blueslime.pixelmotd.motd.builder.hover.platforms;

import com.velocitypowered.api.proxy.server.ServerPing;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import dev.mruniverse.slimelib.file.configuration.TextDecoration;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.hover.HoverModule;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VelocityHover extends HoverModule<ServerPing.SamplePlayer> {
    public VelocityHover(PixelMOTD<?> plugin) {
        super(plugin);
    }

    @Override
    public List<ServerPing.SamplePlayer> generate(ConfigurationHandler configuration, String path, String user, int online, int max) {
        final List<ServerPing.SamplePlayer> sample = new ArrayList<>();
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
                    new ServerPing.SamplePlayer(
                            getExtras().replace(
                                    line, online, max, user
                            ),
                            uuid
                    )
            );
        }

        return sample;
    }

    @Override
    public ServerPing.SamplePlayer[] convert(List<ServerPing.SamplePlayer> list) {
        return list.toArray(new ServerPing.SamplePlayer[0]);
    }
}
