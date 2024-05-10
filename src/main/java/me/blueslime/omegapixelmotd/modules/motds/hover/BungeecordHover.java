package me.blueslime.omegapixelmotd.modules.motds.hover;

import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.omegapixelmotd.modules.motds.Motd;
import me.blueslime.omegapixelmotd.utils.text.TextReplacer;
import me.blueslime.wardenplugin.colors.ColorHandler;
import me.blueslime.wardenplugin.modules.list.BungeecordModule;
import net.md_5.bungee.api.ServerPing;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BungeecordHover extends BungeecordModule {
    public BungeecordHover(OmegaPixelMOTD plugin) {
        super(plugin.cast());
    }

    @Override
    public void initialize() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void reload() {

    }

    public List<ServerPing.PlayerInfo> generate(Motd motd, TextReplacer replacer) {
        final List<ServerPing.PlayerInfo> sample = new ArrayList<>();
        final UUID uuid = new UUID(0, 0);

        List<String> lines = motd.getHover(replacer);

        for (String line : lines) {
            sample.add(
                new ServerPing.PlayerInfo(
                    ColorHandler.convert(line),
                    uuid
                )
            );
        }

        return sample;
    }

    public ServerPing.PlayerInfo[] convert(List<ServerPing.PlayerInfo> list) {
        return list.toArray(new ServerPing.PlayerInfo[0]);
    }
}
