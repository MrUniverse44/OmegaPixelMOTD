package me.blueslime.pixelmotd.listener;

import me.blueslime.pixelmotd.players.PlayerDatabase;

public interface Ping {
    PlayerDatabase database = new PlayerDatabase();

    default PlayerDatabase getPlayerDatabase() {
        return database;
    }

    PingBuilder<?, ?, ?, ?> getPingBuilder();

}
