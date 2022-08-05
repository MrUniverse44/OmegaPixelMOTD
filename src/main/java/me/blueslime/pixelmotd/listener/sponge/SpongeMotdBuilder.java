package me.blueslime.pixelmotd.listener.sponge;

import me.blueslime.pixelmotd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.Icon;
import me.blueslime.pixelmotd.listener.MotdBuilder;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import org.spongepowered.api.Server;
import org.spongepowered.api.network.status.Favicon;

import java.io.File;

public class SpongeMotdBuilder extends MotdBuilder<Server, Favicon> {

    public SpongeMotdBuilder(PixelMOTD<Server> plugin, SlimeLogs logs) {
        super(plugin, logs);
    }

    @Override
    public Icon<Favicon> createIcon(MotdType motdType, File icon) {
        return new SpongeIcon(
                getLogs(),
                motdType,
                icon
        );
    }
}
