package me.blueslime.pixelmotd.utils.ping;

import me.blueslime.pixelmotd.motd.builder.PingBuilder;

public interface Ping {
    PingBuilder<?, ?, ?, ?> getPingBuilder();
}
