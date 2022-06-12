package dev.justjustin.pixelmotd.servers;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class SpigotServerHandler implements ServerHandler {

    private final JavaPlugin plugin;

    public <T> SpigotServerHandler(T plugin) {
        this.plugin = (JavaPlugin) plugin;
    }


    /**
     * Get servers or worlds in the network
     *
     * @return List(Server) Servers or Worlds Names
     */
    @Override
    public List<Server> getServers() {
        List<Server> serverList = new ArrayList<>();

        for (World world : plugin.getServer().getWorlds()) {
            serverList.add(
                    new Server(
                            world.getName(),
                            world.getPlayers().size()
                    )
            );
        }

        return serverList;
    }

    /**
     * Get the amount of players of a specified server
     *
     * @param serverName the server name
     * @return Integer
     */
    @Override
    public int getServerUsers(String serverName) {
        World world = plugin.getServer().getWorld(serverName);

        if (world != null) {
            return world.getPlayers().size();
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
        return plugin.getServer().getWorlds().size();
    }
}
