package me.blueslime.pixelmotd.listener.spigot.version.handlers;

import me.blueslime.pixelmotd.listener.spigot.version.PlayerVersionHandler;
import org.bukkit.entity.Player;

public class None implements PlayerVersionHandler {
    @Override
    public int getProtocol(Player player) {
        return -1;
    }
}
