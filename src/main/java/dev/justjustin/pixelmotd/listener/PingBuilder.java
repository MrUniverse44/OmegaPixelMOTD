package dev.justjustin.pixelmotd.listener;

import dev.justjustin.pixelmotd.MotdType;

public interface PingBuilder<E, H> {

    void update();

    /**
     * @param motdType motdType
     * @param event the event (Example: ProxyPingEvent)
     * @param protocol player protocol version
     * @param username the username
     */
    void execute(MotdType motdType, E event,int protocol, String username);

    H[] getHover(MotdType motdType, int online, int max, String username);
}
