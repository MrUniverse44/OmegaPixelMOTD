package me.blueslime.pixelmotd.players;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import java.util.ArrayList;
import java.util.List;

public class VelocityPlayerHandler implements PlayerHandler {

    private final ProxyServer plugin;

    public <T> VelocityPlayerHandler(T plugin) {
        this.plugin = (ProxyServer) plugin;
    }

    @Override
    public List<String> getPlayersNames() {
        List<String> names = new ArrayList<>();

        int current = 1;
        int max = 10;

        for (Player player : plugin.getAllPlayers()) {
            if (current <= max) {
                names.add(player.getUsername());
            } else {
                return names;
            }
            current++;
        }

        return names;
    }

    @Override
    public int getPlayersSize() {
        return plugin.getPlayerCount();
    }

    @Override
    public int getMaxPlayers() {
        return plugin.getConfiguration().getShowMaxPlayers();
    }
}
