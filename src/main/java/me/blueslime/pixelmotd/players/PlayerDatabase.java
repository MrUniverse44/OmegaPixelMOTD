package me.blueslime.pixelmotd.players;

import java.util.HashMap;
import java.util.Map;

public class PlayerDatabase {

    private final Map<String, String> playersMap = new HashMap<>();

    public boolean exists(String key) {
        return playersMap.containsKey(key);
    }

    public String getPlayer(String key, String def) {
        if (key.contains(":")) {
            return getPlayerFromSocket(key, def);
        }
        return playersMap.computeIfAbsent(key, V -> def);
    }

    public String getPlayerFromSocket(String socket, String def) {
        socket = socket.replace("/","");

        String[] key = socket.split(":");

        return getPlayer(key[0], def);
    }

    public Map<String, String> getPlayersMap() {
        return playersMap;
    }

    public void add(String key, String value) {
        if (playersMap.size() >= 50) {
            clear();
        }
        playersMap.put(key, value);
    }

    public void fromSocket(String socket, String user) {
        socket = socket.replace("/","");

        String[] key = socket.split(":");

        add(key[0], user);
    }

    public void clear() {
        playersMap.clear();
    }

}
