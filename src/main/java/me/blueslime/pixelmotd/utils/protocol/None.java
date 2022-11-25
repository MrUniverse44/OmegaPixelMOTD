package me.blueslime.pixelmotd.utils.protocol;

import org.bukkit.entity.Player;

public class None implements PlayerVersionHandler {
    @Override
    public int getProtocol(Player player) {
        return -1;
    }
}
