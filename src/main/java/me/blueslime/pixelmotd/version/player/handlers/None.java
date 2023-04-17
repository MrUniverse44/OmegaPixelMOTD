package me.blueslime.pixelmotd.version.player.handlers;

import me.blueslime.pixelmotd.version.player.PlayerVersionHandler;
import org.bukkit.entity.Player;

public class None implements PlayerVersionHandler {
    @Override
    public int getProtocol(Player player) {
        return -1;
    }
}
