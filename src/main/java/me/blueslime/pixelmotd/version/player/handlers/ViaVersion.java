package me.blueslime.pixelmotd.version.player.handlers;

import com.viaversion.viaversion.api.Via;
import me.blueslime.pixelmotd.version.player.PlayerVersionHandler;
import org.bukkit.entity.Player;

public class ViaVersion implements PlayerVersionHandler {

    @Override
    public int getProtocol(Player player) {
        return Via.getAPI().getPlayerVersion(player.getUniqueId());
    }
}
