package me.blueslime.pixelmotd.version.player.handlers;

import com.comphenix.protocol.ProtocolLibrary;
import me.blueslime.pixelmotd.version.player.PlayerVersionHandler;
import org.bukkit.entity.Player;

public class ProtocolLib implements PlayerVersionHandler {
    @Override
    public int getProtocol(Player player) {
        return ProtocolLibrary.getProtocolManager().getProtocolVersion(player);
    }
}
