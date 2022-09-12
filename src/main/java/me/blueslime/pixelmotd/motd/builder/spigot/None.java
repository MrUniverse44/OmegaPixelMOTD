package me.blueslime.pixelmotd.motd.builder.spigot;

import org.bukkit.entity.Player;

public class None implements PlayerVersionHandler {
    @Override
    public int getProtocol(Player player) {
        return -1;
    }
}
