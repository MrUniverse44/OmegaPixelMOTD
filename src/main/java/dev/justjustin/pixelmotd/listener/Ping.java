package dev.justjustin.pixelmotd.listener;

import dev.justjustin.pixelmotd.players.PlayerDatabase;

public interface Ping {
    PlayerDatabase database = new PlayerDatabase();

    default PlayerDatabase getPlayerDatabase() {
        return database;
    }

    PingBuilder<?, ?, ?, ?> getPingBuilder();

}
