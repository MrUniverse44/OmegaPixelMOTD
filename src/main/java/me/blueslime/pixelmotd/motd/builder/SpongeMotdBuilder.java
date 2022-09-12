package me.blueslime.pixelmotd.motd.builder;

import me.blueslime.pixelmotd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.icons.Icon;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.motd.builder.icons.sponge.SpongeIcon;
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
