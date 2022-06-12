package dev.justjustin.pixelmotd.servers;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class BungeeServerHandler implements ServerHandler {

    private final Plugin plugin;

    public <T> BungeeServerHandler(T plugin) {
        this.plugin = (Plugin) plugin;
    }


    /**
     * Get servers or worlds in the network
     *
     * @return List(Server) Servers or Worlds Names
     */
    @Override
    public List<Server> getServers() {
        List<Server> serverList = new ArrayList<>();

        for(ServerInfo info : plugin.getProxy().getServers().values()) {
            serverList.add(
                    new Server(
                            info.getName(),
                            info.getPlayers().size()
                    )
            );
        }

        return serverList;
    }

    @Override
    public int getServerUsers(String serverName) {
        if (plugin.getProxy().getServers().containsKey(serverName)) {
            return plugin.getProxy().getServers().get(serverName).getPlayers().size();
        }
        return 0;
    }

    /**
     * Get the amount of servers in the network
     *
     * @return int Servers Size
     */
    @Override
    public int getSize() {
        return plugin.getProxy().getServers().size();
    }
}
