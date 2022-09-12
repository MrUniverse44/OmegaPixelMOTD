package me.blueslime.pixelmotd.motd.builder;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.util.Favicon;
import me.blueslime.pixelmotd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.icons.Icon;
import dev.mruniverse.slimelib.logs.SlimeLogs;
import me.blueslime.pixelmotd.motd.builder.icons.velocity.VelocityIcon;

import java.io.File;

public class VelocityMotdBuilder extends MotdBuilder<ProxyServer, Favicon> {

    public VelocityMotdBuilder(PixelMOTD<ProxyServer> plugin, SlimeLogs logs) {
        super(plugin, logs);
    }

    @Override
    public Icon<Favicon> createIcon(MotdType motdType, File icon) {
        return new VelocityIcon(
                getLogs(),
                motdType,
                icon
        );
    }
}
