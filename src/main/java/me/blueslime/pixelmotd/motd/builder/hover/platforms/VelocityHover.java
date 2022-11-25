package me.blueslime.pixelmotd.motd.builder.hover.platforms;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.motd.builder.hover.HoverController;

import java.util.Collections;
import java.util.List;

public class VelocityHover extends HoverController<ProxyServer, ServerPing.SamplePlayer> {
    public VelocityHover(PixelMOTD<ProxyServer> plugin) {
        super(plugin);
    }

    @Override
    public List<ServerPing.SamplePlayer> fromList(List<String> lines) {
        //TODO: This will be worked in the future
        return Collections.emptyList();
    }
}
