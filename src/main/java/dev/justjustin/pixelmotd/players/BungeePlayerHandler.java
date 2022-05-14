package dev.justjustin.pixelmotd.players;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class BungeePlayerHandler implements PlayerHandler {

    private final Plugin plugin;

    public <T> BungeePlayerHandler(T plugin) {
        this.plugin = (Plugin) plugin;
    }

    @Override
    public List<String> getPlayersNames() {
        List<String> names = new ArrayList<>();

        int current = 1;
        int max = 10;

        for (ProxiedPlayer player : plugin.getProxy().getPlayers()) {
            if (current <= max) {
                names.add(player.getName());
            } else {
                return names;
            }
            current++;
        }

        return names;
    }

    @Override
    public int getPlayersSize() {
        return plugin.getProxy().getOnlineCount();
    }

    @Override
    public int getMaxPlayers() {
        return plugin.getProxy().getConfig().getPlayerLimit();
    }
}
