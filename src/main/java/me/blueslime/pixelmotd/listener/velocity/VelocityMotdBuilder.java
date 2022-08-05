package me.blueslime.pixelmotd.listener.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.util.Favicon;
import me.blueslime.pixelmotd.MotdType;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.listener.Icon;
import me.blueslime.pixelmotd.listener.MotdBuilder;
import dev.mruniverse.slimelib.logs.SlimeLogs;

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
