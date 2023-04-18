package me.blueslime.pixelmotd.servers.platform;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.blueslime.pixelmotd.servers.Server;
import me.blueslime.pixelmotd.servers.ServerHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VelocityServerHandler implements ServerHandler {

    private final ProxyServer plugin;

    public <T> VelocityServerHandler(T plugin) {
        this.plugin = (ProxyServer) plugin;
    }


    /**
     * Get servers or worlds in the network
     *
     * @return List(Server) Servers or Worlds Names
     */
    @Override
    public List<Server> getServers() {
        List<Server> serverList = new ArrayList<>();

        for (RegisteredServer server : plugin.getAllServers()) {
            serverList.add(
                    new Server(
                        server.getServerInfo().getName(),
                        server.getPlayersConnected().size()
                    )
            );
        }

        return serverList;
    }

    @Override
    public int getServerUsers(String serverName) {
        Optional<RegisteredServer> optional = plugin.getServer(serverName);
        return optional.map(registeredServer -> registeredServer.getPlayersConnected().size()).orElse(0);
    }

    /**
     * Get the amount of servers in the network
     *
     * @return int Servers Size
     */
    @Override
    public int getSize() {
        return plugin.getAllServers().size();
    }
}
