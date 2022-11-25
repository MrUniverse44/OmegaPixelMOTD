package me.blueslime.pixelmotd.utils.protocol;

import com.viaversion.viaversion.api.Via;
import org.bukkit.entity.Player;

public class ViaVersion implements PlayerVersionHandler {

    @Override
    public int getProtocol(Player player) {
        return Via.getAPI().getPlayerVersion(player.getUniqueId());
    }
}
