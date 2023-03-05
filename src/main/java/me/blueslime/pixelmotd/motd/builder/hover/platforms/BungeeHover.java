package me.blueslime.pixelmotd.motd.builder.hover.platforms;

import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.hover.HoverModule;
import net.md_5.bungee.api.ServerPing;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BungeeHover extends HoverModule<ServerPing.PlayerInfo> {
    public BungeeHover(PixelMOTD<?> plugin) {
        super(plugin);
    }

    @Override
    public List<ServerPing.PlayerInfo> generate(List<String> lineList, String user, int online, int max) {
        final List<ServerPing.PlayerInfo> sample = new ArrayList<>();
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
                    new ServerPing.PlayerInfo(
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
    public ServerPing.PlayerInfo[] convert(List<ServerPing.PlayerInfo> list) {
        return list.toArray(new ServerPing.PlayerInfo[0]);
    }
}
