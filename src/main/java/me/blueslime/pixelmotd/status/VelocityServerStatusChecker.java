package me.blueslime.pixelmotd.status;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import com.velocitypowered.api.scheduler.Scheduler;
import dev.mruniverse.slimelib.colors.platforms.velocity.DefaultSlimeColor;
import me.blueslime.pixelmotd.PixelMOTD;
import me.blueslime.pixelmotd.SlimeFile;
import me.blueslime.pixelmotd.initialization.velocity.VelocityMOTD;
import dev.mruniverse.slimelib.file.configuration.ConfigurationHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/*
 * This code was created by Phoenix616
 * And modified a little by me
 * Please give credits to him, this option was added by this code.
 */
@SuppressWarnings("unused")
public class VelocityServerStatusChecker implements StatusChecker {
    private final PixelMOTD<ProxyServer> plugin;

    private final List<ScheduledTask> pingTask = new ArrayList<>();

    private Map<String, Boolean> statusMap = new ConcurrentHashMap<>();

    private ConfigurationHandler control;

    private int pingTimeout = 500;

    private String online;

    private String offline;

    public VelocityServerStatusChecker(PixelMOTD<ProxyServer> plugin) {
        this.plugin = plugin;
        load();
    }

    private void load() {
        this.control = plugin.getConfigurationHandler(SlimeFile.SETTINGS);
        online = legacy(control.getString("settings.server-status.online","&a&lONLINE"));
        offline = legacy(control.getString("settings.server-status.offline","&c&lOFFLINE"));
    }

    public void update() {
        load();
    }

    private @NotNull String legacy(String content) {
        Component color = new DefaultSlimeColor(content, true)
                .build();

        return LegacyComponentSerializer.legacySection().serialize(
                color
        );
    }

    public void start() {
        stop();
        int pingOnline = control.getInt("settings.server-status.intervals.online", 10);
        int pingOffline = control.getInt("settings.server-status.intervals.offline", 10);
        pingTimeout = control.getInt("settings.server-status.intervals.timeout", 500);

        ProxyServer server = plugin.getPlugin();
        VelocityMOTD motd = VelocityMOTD.getInstance();

        if (pingOnline == pingOffline) {
            if (pingOnline == 0)
                return;

            Scheduler.TaskBuilder builder = server.getScheduler().buildTask(motd, () -> {
                if (control.getStatus("settings.server-status.toggle")) {
                    refreshStatusMap(server.getAllServers());
                } else {
                    stop();
                }
            }
            );

            builder.delay(pingOnline, TimeUnit.SECONDS);

            pingTask.add(builder.schedule());
        } else {
            refreshStatusMap(server.getAllServers());
            if (pingOnline != 0) {
                Scheduler.TaskBuilder builder = server.getScheduler().buildTask(motd, () -> {
                            if (control.getStatus("settings.server-status.toggle")) {
                                refreshStatusMap(getOnlineServers());
                            } else {
                                stop();
                            }
                        }
                );
                builder.delay(pingOnline, TimeUnit.SECONDS);

                pingTask.add(builder.schedule());
            }
        }
    }

    public Collection<RegisteredServer> getOnlineServers() {
        List<RegisteredServer> onlineServers = new ArrayList<>();

        for (Map.Entry<String, Boolean> entry : getStatusMap().entrySet()) {
            if (entry.getValue() != null && entry.getValue()) {
                Optional<RegisteredServer> server = plugin.getPlugin().getServer(entry.getKey());

                server.ifPresent(onlineServers::add);
            }
        }
        return onlineServers;
    }

    public void refreshStatusMap(Collection<RegisteredServer> servers) {
        for (final RegisteredServer server : servers) {
            if (server.getPlayersConnected().size() == 0) {
                Scheduler.TaskBuilder builder = plugin.getPlugin().getScheduler().buildTask(
                        VelocityMOTD.getInstance(),
                        () -> setStatus(server, isReachable(server.getServerInfo().getAddress()))
                );

                builder.schedule();
            } else {
                setStatus(server, true);
            }
        }
    }

    private boolean isReachable(InetSocketAddress address) {
        Socket socket = new Socket();
        try {
            socket.connect(address, pingTimeout);
            socket.close();
            return true;
        } catch(IOException ignored) {
            return false;
        }
    }

    private void setStatus(RegisteredServer server, boolean online) {
        statusMap.put(server.getServerInfo().getName(), online);
    }

    public Boolean getStatus(RegisteredServer server) {
        return statusMap.get(server.getServerInfo().getName());
    }

    public void stop() {
        statusMap = new ConcurrentHashMap<>();
        Iterator<ScheduledTask> taskIt = pingTask.iterator();
        while(taskIt.hasNext()) {
            taskIt.next().cancel();
            taskIt.remove();
        }
    }

    public Map<String, Boolean> getStatusMap() {
        return statusMap;
    }

    public String getServerStatus(String server) {
        if (statusMap.containsKey(server)) {
            boolean status = statusMap.get(server);
            if (status) {
                return online;
            }
            return offline;
        }
        return offline;
    }
}

