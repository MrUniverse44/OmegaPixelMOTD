package me.blueslime.pixelmotd.motd.builder.spigot;

import org.bukkit.entity.Player;

public interface PlayerVersionHandler {

    /*
     * Get the protocol version of a player.
     *
     * @param player the user to get the protocol id
     * @return int  - Protocol version
     */
    int getProtocol(Player player);
}
