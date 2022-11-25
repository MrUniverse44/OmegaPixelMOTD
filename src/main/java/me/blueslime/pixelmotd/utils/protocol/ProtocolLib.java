package me.blueslime.pixelmotd.utils.protocol;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.entity.Player;

public class ProtocolLib implements PlayerVersionHandler {
    @Override
    public int getProtocol(Player player) {
        return ProtocolLibrary.getProtocolManager().getProtocolVersion(player);
    }
}
