package me.blueslime.pixelmotd.listener.motd;

import dev.mruniverse.slimelib.control.Control;
import dev.mruniverse.slimelib.utils.CenterText;

import java.util.ArrayList;
import java.util.List;

public class MotdServer {

    private final List<String> serverList = new ArrayList<>();

    private final List<String> motdLines = new ArrayList<>();

    private final String type;

    public MotdServer(Control control, String server) {
        this.serverList.add(server);

        this.serverList.addAll(
                control.getStringList("join-motds." + server + ".more-servers")
        );

        this.motdLines.addAll(
                control.getStringList("join-motds." + server + ".lines")
        );

        this.type = control.getString("join-motds." + server + ".server-names-check-type", "EQUALS");
    }

    public boolean hasServer(String current) {
        if (type.equalsIgnoreCase("EQUALS")) {
            return serverList.contains(current);
        }
        for (String server : serverList) {
            if (current.contains(server)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getLines(String current, String previous, String username) {
        List<String> improved = new ArrayList<>(serverList);

        improved.replaceAll(line ->
                line = center(
                        line.replace("%previous_server%", previous)
                                .replace("%connected_server%", current)
                                .replace("%username%", username)
                                .replace("%player%", username)
                                .replace("%nick%", username)
                                .replace("%user%", username)
                )
        );

        return improved;
    }

    private String center(String line) {
        if (line.contains("<center>")) {
            return CenterText.sendToCenter(
                    line.replace("<center>", "")
            );
        }
        return line;
    }
}
