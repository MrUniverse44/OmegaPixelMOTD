package me.blueslime.omegapixelmotd.modules.motds.hover;

import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.omegapixelmotd.modules.motds.Motd;
import me.blueslime.omegapixelmotd.utils.text.TextReplacer;
import me.blueslime.wardenplugin.colors.ColorHandler;
import me.blueslime.wardenplugin.modules.list.BukkitModule;
import com.comphenix.protocol.wrappers.WrappedGameProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProtocolHover extends BukkitModule {
    public ProtocolHover(OmegaPixelMOTD plugin) {
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

    public List<WrappedGameProfile> generate(Motd motd, TextReplacer replacer) {
        final List<WrappedGameProfile> sample = new ArrayList<>();
        final UUID uuid = new UUID(0, 0);

        List<String> lines = motd.getHover(replacer);

        for (String line : lines) {
            sample.add(
                new WrappedGameProfile(
                    uuid,
                    ColorHandler.convert(line)
                )
            );
        }

        return sample;
    }

    public WrappedGameProfile[] convert(List<WrappedGameProfile> list) {
        return list.toArray(new WrappedGameProfile[0]);
    }
}

