package me.blueslime.omegapixelmotd.modules.listeners.ping;

import me.blueslime.omegapixelmotd.OmegaPixelMOTD;
import me.blueslime.omegapixelmotd.modules.configurations.Configurations;
import me.blueslime.omegapixelmotd.modules.listeners.PluginListener;
import me.blueslime.omegapixelmotd.modules.motds.Motd;
import me.blueslime.omegapixelmotd.modules.motds.MotdData;
import me.blueslime.wardenplugin.configuration.ConfigurationHandler;
import me.blueslime.wardenplugin.utils.PluginConsumer;
import org.spongepowered.api.network.status.Favicon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class SpongePingListener extends PluginListener {
    private final Map<MotdData.Type, List<Motd>> motdCache = new ConcurrentHashMap<>();
    private final Map<String, Favicon> iconCache = new ConcurrentHashMap<>();


    public SpongePingListener(OmegaPixelMOTD plugin) {
        super(plugin);
    }

    @Override
    public void initialize() {
        iconCache.clear();
        motdCache.clear();

        initializeIcons();
        initializeMotds();
    }

    public void initializeMotds() {
        ConfigurationHandler settings = plugin.getModule(Configurations.class).getMotds();

        getLogs().info("Loading motds");

        for (MotdData.Type type : MotdData.Type.values()) {
            List<Motd> list = motdCache.computeIfAbsent(
                    type,
                    (k) -> new ArrayList<>()
            );
            list.clear();
        }

        for (String key : settings.getKeySet(false)) {
            String path = key + ".";

            MotdData.Type type = MotdData.Type.fromString(
                    settings.getString(path + "available-for", "default")
            );

            motdCache.get(type).add(new Motd(settings, key));
        }
    }

    public void initializeIcons() {
        PluginConsumer.process(
                () -> {
                    File data = new File(plugin.getDataFolder(), "icons");

                    if (!data.exists() && !data.mkdirs()) {
                        getLogs().error("Can't initialize icons folder");
                    }

                    File[] files = data.listFiles((d, fn) -> fn.endsWith(".png"));

                    if (files == null) {
                        return;
                    }

                    for (File icon : files) {
                        PluginConsumer.process(
                                () -> {
                                    BufferedImage image = ImageIO.read(icon);

                                    Favicon serverIcon = Favicon.load(image);

                                    iconCache.put(
                                        icon.getName(),
                                        serverIcon
                                    );
                                },
                                e -> getLogs().info("Can't load icon: " + icon.getName())
                        );
                    }
                }
        );
    }

    @Override
    public void reload() {
        iconCache.clear();
        motdCache.clear();

        initializeIcons();
        initializeMotds();
    }

    @Override
    public void shutdown() {
        iconCache.clear();
        motdCache.clear();
    }

    public Motd fetchMotd(MotdData.Type type, int protocol) {
        return fetchMotd(type, protocol, type);
    }

    public Motd fetchMotd(MotdData.Type type, int protocol, MotdData.Type emergencyType) {
        if (type != MotdData.Type.OUTDATED_CLIENT && type != MotdData.Type.OUTDATED_SERVER) {
            ConfigurationHandler settings = plugin.getModule(Configurations.class).getSettings();

            int max = settings.getInt("settings.max-server-protocol", 770);
            int min = settings.getInt("settings.min-server-protocol", 47);

            if (protocol >= min && protocol >= max) {
                type = MotdData.Type.OUTDATED_SERVER;
            }

            if (protocol < min) {
                type = MotdData.Type.OUTDATED_CLIENT;
            }
        }

        List<Motd> motds = motdCache.get(type);
        if (motds == null) {
            getLogs().info("No motds found for MotdType: " + type.toString());
            getLogs().info("Checking back to the first type: " + type.toString());
            if (type != emergencyType) {
                type = emergencyType;
            }
        }
        motds = motdCache.get(type);
        if (motds == null) {
            getLogs().info("Can't found motds for type: " + type.toString());
            getLogs().info("Changing display priority list.");
            type = MotdData.Type.switchPriority(type);
        }
        if (motds == null) {
            getLogs().info("No motds found for MotdType: " + type.toString());
            return null;
        }
        if (motds.isEmpty()) {
            getLogs().info("No motds found at: " + type.toString() + ", changing display priority list.");
            type = MotdData.Type.switchPriority(type);
        }
        motds = motdCache.get(type);
        if (motds == null) {
            getLogs().info("No motds found for MotdType: " + type.toString());
            return null;
        }
        List<Motd> possibleShow = new ArrayList<>();
        for (Motd motd : motds) {
            if (!motd.hasProtocols() || motd.getMinProtocol() == -1 || motd.getMaxProtocol() == -1) {
                possibleShow.add(motd);
                continue;
            }
            if (motd.getMinProtocol() >= protocol && protocol <= motd.getMaxProtocol()) {
                possibleShow.add(motd);
            }
        }
        if (possibleShow.isEmpty() && type != MotdData.Type.NORMAL) {
            return fetchMotd(MotdData.Type.NORMAL, protocol);
        }
        if (possibleShow.isEmpty()) {
            getLogs().info("No motds found at: " + type + ".");
            return null;
        }
        return possibleShow.size() == 1 ? possibleShow.get(0) : possibleShow.get(ThreadLocalRandom.current().nextInt(possibleShow.size()));
    }


    public Favicon getFavicon(String key) {
        if (key.equalsIgnoreCase("RANDOM")) {
            List<Favicon> values = new ArrayList<>(iconCache.values());
            int randomIndex = ThreadLocalRandom.current().nextInt(values.size());
            return values.get(randomIndex);
        }
        if (iconCache.containsKey(key)) {
            return iconCache.get(key);
        }
        return null;
    }

}
