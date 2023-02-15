package me.blueslime.pixelmotd.servers;

import dev.mruniverse.slimelib.SlimePlatform;

import java.util.List;

public interface ServerHandler {

    /**
     * Get servers or worlds in the network
     * @return List(Server) Servers or Worlds Names
     */
    List<Server> getServers();

    /**
     * Get the name of a specified server
     * @return Integer
     */
    default int getServerUsers(String serverName) {
        for (Server server : getServers()) {
            if (server.getName().equalsIgnoreCase(serverName)) {
                return server.getOnline();
            }
        }
        return 0;
    }

    /**
     * Get the amount of servers in the network
     * @return int Servers Size
     */
    int getSize();

    static <T> ServerHandler fromPlatform(SlimePlatform platform, T plugin) {
        switch (platform) {
            case SPIGOT:
                return new SpigotServerHandler(plugin);
            case VELOCITY:
                return new VelocityServerHandler(plugin);
            default:
            case BUNGEECORD:
                return new BungeeServerHandler(plugin);
        }
    }
}
