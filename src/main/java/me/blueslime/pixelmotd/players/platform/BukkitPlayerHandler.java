package me.blueslime.pixelmotd.players.platform;

import me.blueslime.pixelmotd.players.PlayerHandler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class BukkitPlayerHandler implements PlayerHandler {

    private final JavaPlugin plugin;

    public <T> BukkitPlayerHandler(T plugin) {
        this.plugin = (JavaPlugin) plugin;
    }

    @Override
    public List<String> getPlayersNames() {
        List<String> names = new ArrayList<>();

        int current = 1;
        int max = 10;

        for (Player player : plugin.getServer().getOnlinePlayers()) {
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
        return plugin.getServer().getOnlinePlayers().size();
    }

    @Override
    public int getMaxPlayers() {
        return plugin.getServer().getMaxPlayers();
    }
}