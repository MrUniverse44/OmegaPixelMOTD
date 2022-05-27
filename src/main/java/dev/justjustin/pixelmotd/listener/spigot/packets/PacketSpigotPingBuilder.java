package dev.justjustin.pixelmotd.listener.spigot.packets;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import dev.justjustin.pixelmotd.MotdType;
import dev.justjustin.pixelmotd.PixelMOTD;
import dev.justjustin.pixelmotd.listener.MotdBuilder;
import dev.justjustin.pixelmotd.listener.PingBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketSpigotPingBuilder extends PingBuilder<JavaPlugin, WrappedServerPing.CompressedImage, WrappedServerPing, WrappedGameProfile> {

    private final boolean hasPAPI;

    public PacketSpigotPingBuilder(PixelMOTD<JavaPlugin> plugin, MotdBuilder<JavaPlugin, WrappedServerPing.CompressedImage> builder) {
        super(plugin, builder);
        hasPAPI = plugin.getPlugin().getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    @Override
    public void execute(MotdType motdType, WrappedServerPing ping, int code, String user) {
        //TODO: Execute
    }

    @Override
    public WrappedGameProfile[] getHover(MotdType motdType, String path, int online, int max, String user) {
        //TODO: Get Hover
        return null;
    }

    @Override
    public WrappedGameProfile[] addHoverLine(WrappedGameProfile[] player, WrappedGameProfile info) {
        //TODO: Add Hover Line
        return null;
    }
}
